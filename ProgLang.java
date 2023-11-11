package zad3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProgLang {
    private String file;
    private Map<String, Set<String>> languageMap;
    private Map<String, Set<String>> programmersMap;
    public ProgLang(String file){
        this.file = file;
        languageMap = new LinkedHashMap<>();
        programmersMap = new LinkedHashMap<>();
    }
    public Map<String, Set<String>> getLangsMap(){
        try(BufferedReader br = new BufferedReader(new FileReader(new File(file)))){
            String line;
            while((line = br.readLine()) != null) {
                String[] part = line.split("\t", 2);
                String language = part[0];
                Set<String> programmers = new LinkedHashSet<>(Arrays.asList(part[1].split("\t")));
                languageMap.put(language, programmers);
            }
            return languageMap;
        }catch (IOException e){
            System.out.println(e.getStackTrace());
        }
        return null;
    }
    public Map<String, Set<String>> getProgsMap(){
        try(BufferedReader br = new BufferedReader(new FileReader(new File(file)))){
            String line;
            while((line = br.readLine()) != null){
                String[] part = line.split("\t", 2);
                String language = part[0];
                Set<String> programmers = new LinkedHashSet<>(Arrays.asList(part[1].split("\t")));
                for(String p : programmers){
                    programmersMap.computeIfAbsent(p, k -> new LinkedHashSet<>()).add(language);
                }
            }
            return programmersMap;
        }catch (IOException e){
            System.out.println(e.getStackTrace());
        }
        return null;
    }
    public Map<String, Set<String>> getLangsMapSortedByNumOfProgs() {
        Comparator<Map.Entry<String, Set<String>>> comparator = Comparator
                .comparingInt((Map.Entry<String, Set<String>> e) -> e.getValue().size())
                .thenComparing(Map.Entry::getKey);

        return sorted(languageMap, comparator);
    }

    public Map<String, Set<String>> getProgsMapSortedByNumOfLangs(){
        Comparator<Map.Entry<String, Set<String>>> comparator = Comparator
                .comparingInt((Map.Entry<String, Set<String>> e) -> e.getValue().size())
                .thenComparing(Map.Entry::getKey);

        return sorted(programmersMap, comparator);
    }
    public Map<String, Set<String>> getProgsMapForNumOfLangsGreaterThan(int n){
        return filtered(programmersMap, entry -> entry.getValue().size() > n);
    }

    private <K, V> Map<K, V> sorted(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator){
        return map.entrySet()
                .stream()
                .sorted(comparator)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
    }
    private <K, V> Map<K, V> filtered(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
        return map.entrySet()
                .stream()
                .filter(predicate)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
