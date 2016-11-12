package ru.luvas.dk.server.neural;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import ru.luvas.dk.server.DarlingKate;
import ru.luvas.dk.server.configuration.ConfigurationSection;
import ru.luvas.dk.server.configuration.FileConfiguration;
import ru.luvas.dk.server.util.Logger;
import weka.classifiers.functions.SMO;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class Classifier {
    
    private Instances data;
    private StringToWordVector filter = new StringToWordVector();
    private weka.classifiers.Classifier cls = new SMO();
    
    private final Random r = new Random();
    private final Map<String, List<String>> clusters = new HashMap<>();
    private final Map<String, String> learned = new HashMap<>();
    
    private boolean isUpToDate = false;
    
    private final Map<String, List<String>> newClusters = new HashMap<>();
    private final Map<String, String> newLearned = new HashMap<>();
    
    private static String parse(String s) {
        s = s.toLowerCase();
        s = s.replace(".", "").replace(",", "").replace("-", "").replace("!", "").replace("?", "");
        s = s.replace(":", "").replace(";", "");
        return s;
    }

    public Classifier() {
        FastVector attributes = new FastVector(), classes = new FastVector();
        attributes.addElement(new Attribute("Message", (FastVector) null));
        FileConfiguration config = getConfig();
        if(!config.isSet("config_version")) {
            config.set("config_version", 1);
            saveConfig();
        }
        ConfigurationSection cs = config.getConfigurationSection("clusters");
        if(cs != null)
            cs.getKeys(false).forEach(key -> {
                clusters.put(key, cs.getStringList(key));
                classes.addElement(key);
            });
        attributes.addElement(new Attribute("Class", classes));
        data = new Instances("wbb", attributes, DarlingKate.getWekaCapacity());
        data.setClassIndex(data.numAttributes() - 1);
        ConfigurationSection cs2 = config.getConfigurationSection("words");
        if(cs2 != null)
            cs2.getKeys(false).forEach(key -> study(key, cs2.getString(key)));
    }
    
    public void invalidate() {
        data = null;
        filter = null;
        cls = null;
        clusters.clear();
        learned.clear();
        newClusters.clear();
        newLearned.clear();
    }
    
    public void save() {
        FileConfiguration config = getConfig();
        newLearned.keySet().forEach(word -> config.set("words." + word, newLearned.get(word)));
        newClusters.keySet().forEach(cluster -> {
            List<String> list = getFullCluster(cluster);
            config.set("clusters." + cluster, list);
        });
        saveConfig();
    }
    
    public void removeCluster(String cluster) {
        cluster = cluster.toLowerCase();
        clusters.remove(cluster);
        newClusters.remove(cluster);
        FileConfiguration config = getConfig();
        config.set("clusters." + cluster, null);
        saveConfig();
    }
    
    public void removePhrase(String phrase) {
        phrase = parse(phrase);
        learned.remove(phrase);
        newLearned.remove(phrase);
        FileConfiguration config = getConfig();
        config.set("words." + phrase, null);
        saveConfig();
    }
    
    public void learnMirror(String phrase, String anotherPhrase) {
        phrase = parse(phrase);
        anotherPhrase = parse(anotherPhrase);
        if(learned.containsKey(phrase) || newLearned.containsKey(phrase))
            return;
        String cluster = getClusterName(phrase);
        if(cluster == null)
            return;
        learn(phrase, cluster);
    }
    
    public void learn(String phrase, String cluster) {
        cluster = cluster.toLowerCase();
        phrase = parse(phrase);
        if(learned.containsKey(phrase) || newLearned.containsKey(phrase))
            return;
        newLearned.put(phrase, cluster);
    }
    
    public void updateCluster(String name, String newPhrase) {
        name = name.toLowerCase();
        List<String> cluster = getFullCluster(name);
        if(cluster.contains(newPhrase))
            return;
        List<String> list = newClusters.get(name);
        if(list == null) {
            list = new ArrayList<>();
            newClusters.put(name, list);
        }
        list.add(newPhrase);
    }
    
    public String getClusterName(String phrase) {
        phrase = parse(phrase);
        String cluster = learned.get(phrase);
        if(cluster == null)
            cluster = newLearned.get(phrase);
        return cluster;
    }
    
    public Collection<String> getAllClustersNames() {
        Set<String> result = new HashSet<>();
        result.addAll(clusters.keySet());
        result.addAll(newClusters.keySet());
        return result;
    }
    
    public List<String> getFullCluster(String name) {
        name = name.toLowerCase();
        List<String> list = new ArrayList<>(), list2 = clusters.get(name);
        if(list2 != null)
            list.addAll(list2);
        list2 = newClusters.get(name);
        if(list2 != null)
            list.addAll(list2);
        return list;
    }

    private void study(String message, String handler) {
        try {
            learned.put(message, handler);
            Instance instance = makeInstance(message, data);
            instance.setClassValue(handler);
            data.add(instance);
            isUpToDate = false;
        } catch (Exception ex) {
            Logger.warn("Can't study for \"" + handler + "\"!", ex);
        }
    }

    public String classify(String message) {
        message = parse(message);
        try {
            if(!isUpToDate) {
                filter.setInputFormat(data);
                Instances filteredData = Filter.useFilter(data, filter);
                cls.buildClassifier(filteredData);
                isUpToDate = true;
            }
            Instances testset = data.stringFreeStructure();
            Instance instance = makeInstance(message, testset);
            filter.input(instance);
            instance = filter.output();
            String cluster = data.classAttribute().value((int) cls.classifyInstance(instance));
            List<String> list = clusters.get(cluster);
            return list.get(r.nextInt(list.size()));
        } catch (Exception ex) {
            return null;
        }
    }

    private Instance makeInstance(String text, Instances data) {
        Instance instance = new Instance(2);
        Attribute messageAtt = data.attribute("Message");
        instance.setValue(messageAtt, messageAtt.addStringValue(text));
        instance.setDataset(data);
        return instance;
    }
    
    private static FileConfiguration getConfig() {
        return DarlingKate.getInstance().getConfig("weka");
    }
    
    private static void saveConfig() {
        DarlingKate.getInstance().saveConfig("weka");
    }

}
