package day20;

import java.util.*;
import java.util.stream.Collectors;

public class EncryptedFile {
    private static final boolean DEBUG = false;

    private final List<Integer> encryptedNumbers;
    private Iterator<Integer> iterator;

    public EncryptedFile() {
        this.encryptedNumbers = new LinkedList<>();
    }

    public void add(int number) {
        this.encryptedNumbers.add(number);
    }

    public int computeGroveCoordinate() {
        List<Integer> remixedNumbers = this.mix();
        //System.out.println("Remixed numbers : " + remixedNumbers);
        iterator = remixedNumbers.iterator();

        // Find 0 number
        while (iterator.hasNext()) {
            int number = iterator.next();
            if (number == 0)
                break;
        }

        int groveCoordinate = 0;
        int i = 1;
        while (i <= 3000) {
            int number = this.nextRemixedNumber(remixedNumbers);
            if (i == 1000 || i == 2000 || i == 3000) {
                System.out.println(i + "th number is " + number);
                groveCoordinate += number;
            }
            i++;
        }

        return groveCoordinate;
    }

    private int nextRemixedNumber(List<Integer> remixedNumber) {
        if (iterator.hasNext())
            return iterator.next();
        iterator = remixedNumber.iterator();
        return iterator.next();
    }

    private List<Integer> mix() {
        List<Integer> remixedNumbers = new LinkedList<>(this.encryptedNumbers);

        Map<Integer, Integer> reversedIndexMapping = new HashMap<>();
        // init mapping
        for (int i = 0; i< this.encryptedNumbers.size(); i++) {
            reversedIndexMapping.put(i, i);
        }

        for (int i = 0; i< this.encryptedNumbers.size(); i++) {
            int number = this.encryptedNumbers.get(i);
            System.out.println("### Moving " + number + " ###");
            int realIndexOfCurrentNumberInRemixedNumbers = invertMap(reversedIndexMapping).get(i);
            if (DEBUG)
                System.out.println("This number is at index " + realIndexOfCurrentNumberInRemixedNumbers + " in remixed numbers " + remixedNumbers);
            int circularedMove = circularNumber(number);
            if (circularedMove != number) {
                circularedMove--;
            }
            int newIndexToMoveTo = circularNumber(realIndexOfCurrentNumberInRemixedNumbers + circularedMove);
            int tempIndexToMoveTo = newIndexToMoveTo;
            if (circularedMove > 0) {
                tempIndexToMoveTo++;
                if (tempIndexToMoveTo <= realIndexOfCurrentNumberInRemixedNumbers)
                    newIndexToMoveTo = tempIndexToMoveTo;
            }
            if (newIndexToMoveTo != realIndexOfCurrentNumberInRemixedNumbers) {
                if (DEBUG)
                    System.out.println("First we should move it temporarly to index " + tempIndexToMoveTo);
                remixedNumbers.add(tempIndexToMoveTo, number);
                if (DEBUG)
                    System.out.println("Remixed numbers : " + remixedNumbers);
                int tempRealIndexOfCurrentNumberInRemixedNumbers = realIndexOfCurrentNumberInRemixedNumbers;
                if (tempIndexToMoveTo < realIndexOfCurrentNumberInRemixedNumbers) {
                    tempRealIndexOfCurrentNumberInRemixedNumbers++;
                }
                if (DEBUG)
                    System.out.println("Then we should remove it from current index " + tempRealIndexOfCurrentNumberInRemixedNumbers);
                remixedNumbers.remove(tempRealIndexOfCurrentNumberInRemixedNumbers);
            }
            else {
                if (DEBUG)
                    System.out.println("No need to move as target index " + newIndexToMoveTo + " is the same as source index "  +realIndexOfCurrentNumberInRemixedNumbers);
            }
            System.out.println("Remixed numbers : " + remixedNumbers);
            if (DEBUG)
                System.out.println("S:" + realIndexOfCurrentNumberInRemixedNumbers + " T:" + newIndexToMoveTo + " M:" + (newIndexToMoveTo - realIndexOfCurrentNumberInRemixedNumbers));

            //this.checkNumbers(remixedNumbers);

            if (DEBUG)
                System.out.println("Now we update index mapping starting from " + reversedIndexMapping);
            if (newIndexToMoveTo > realIndexOfCurrentNumberInRemixedNumbers) {
                HashMap<Integer, Integer> reversedOverridenMap = new HashMap<>();
                reversedOverridenMap.put(newIndexToMoveTo, reversedIndexMapping.get(realIndexOfCurrentNumberInRemixedNumbers));
                for (int j = realIndexOfCurrentNumberInRemixedNumbers; j < newIndexToMoveTo; j++) {
                    if ((j+1) >= this.encryptedNumbers.size()) {
                        System.out.println("####### WARNING : J > file size ########");
                        System.exit(1);
                    }
                    reversedOverridenMap.put(circularNumber(j), reversedIndexMapping.get(j+1));
                }
                reversedIndexMapping.putAll(reversedOverridenMap);

            }
            else if (newIndexToMoveTo < realIndexOfCurrentNumberInRemixedNumbers) {
                HashMap<Integer, Integer> reversedOverridenMap = new HashMap<>();
                reversedOverridenMap.put(newIndexToMoveTo, reversedIndexMapping.get(realIndexOfCurrentNumberInRemixedNumbers));
                for (int j = newIndexToMoveTo; j < realIndexOfCurrentNumberInRemixedNumbers; j++) {
                    if ((j+1) >= this.encryptedNumbers.size()) {
                        System.out.println("####### WARNING : J > file size ########");
                        System.exit(1);
                    }
                    reversedOverridenMap.put(circularNumber(j+1), reversedIndexMapping.get(j));
                }
                reversedIndexMapping.putAll(reversedOverridenMap);
            }

            if (DEBUG)
                System.out.println("Now updated index mapping is : " + reversedIndexMapping);
            if (DEBUG)
                System.out.println(reversedIndexMapping.values());
            //this.checkMapping(reversedIndexMapping);
        }

        return remixedNumbers;
    }

    private void checkMapping(Map<Integer, Integer> reversedIndexMapping) {
        Set<Integer> keys = reversedIndexMapping.keySet();
        if (this.encryptedNumbers.size() > keys.size()) {
            System.out.println("WTF Keys !!!");
            System.exit(1);
        }
        keys.forEach(index -> {
            if (index < 0 || index >= this.encryptedNumbers.size()) {
                System.out.println("Key " + index + " of index mapping is out of range : 0-" + (this.encryptedNumbers.size() - 1));
                System.exit(1);
            }
        });

        Collection<Integer> values = reversedIndexMapping.values();
        if (this.encryptedNumbers.size() > new HashSet<>(values).size()) {
            System.out.println("mapping size " + reversedIndexMapping.size());
            System.out.println("values of index mapping size " + new HashSet<>(values).size());
            System.out.println("WTF Values !!!");
            System.exit(1);
        }
        values.forEach(index -> {
            if (index < 0 || index >= this.encryptedNumbers.size()) {
                System.out.println("Value " + index + " of index mapping is out of range : 0-" + (this.encryptedNumbers.size() - 1));
                System.exit(1);
            }
        });
    }

    private void checkNumbers(List<Integer> remixedNumbers) {
        int sum = this.encryptedNumbers.stream().reduce(0, Integer::sum);
        int remixedSum = remixedNumbers.stream().reduce(0, Integer::sum);
        if (remixedSum != sum) {
            System.out.println("Sum of remixed numbers " + remixedSum + " is different from original one " + sum);
            System.exit(1);
        }

        this.encryptedNumbers.forEach(number -> {
            if (! remixedNumbers.contains(number)) {
                System.out.println("Number " + number + " is not present in remixed numbers");
                System.exit(1);
            }

            long nbNumber = this.encryptedNumbers.stream().filter(n -> n.equals(number)).count();
            long nbNumberInRemixed = remixedNumbers.stream().filter(n -> n.equals(number)).count();
            if (nbNumber != nbNumberInRemixed) {
                System.out.println("Nb occurence of number " + number + " is different in remixed numbers " + nbNumberInRemixed + " comparing to original one " + nbNumber);
                System.exit(1);
            }
        });
    }

    private static Map<Integer, Integer> invertMap(Map<Integer, Integer> map) {
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private int circularNumber(int number) {
        int totalNumbers = this.encryptedNumbers.size();
        if (number >= 0) {
            return number % totalNumbers;
        }
        return circularNumber(number % totalNumbers + totalNumbers);
    }

    @Override
    public String toString() {
        return "EncryptedFile{" +
                "encryptedNumbers=" + encryptedNumbers +
                '}';
    }
}
