import java.util.*;


public class BDD {

    BDDNode low;
    BDDNode high;
    BDDNode root;

    HashMap<String, BDDNode> nodeMap = new HashMap<>();



    public static String[] splitBfunc(String input) {
        String delimiter = "\\s*\\+\\s*";
        return input.split(delimiter);
    }

    public static boolean checkOneLetter(String[] arr) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        if (arr.length == 1) {
            String str = arr[0];
            if (str.length() == 1) {
                return true;
            } else if (str.length() == 2 && str.charAt(0) == '!') {
                return true;
            }
        } else if (arr.length > 2) {
            return false;
        } else {
            String first = arr[0];
            String second = arr[1];
            if (first.length() == 2 && second.length() == 2) {
                return false;
            } else if (first.length() > 2 && second.length() > 2) {
                return false;
            } else if (first.length() == 2) {
                String opposite = first.substring(1);
                return !second.equals(opposite);
            } else if (second.length() == 2) {
                String opposite = second.substring(1);
                return !first.equals(opposite);
            } else {
                return true;
            }
        }
        return false;
    }
    public static String[] sortStrings(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].replace(".", "");
        }
        Arrays.sort(arr, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                String normalized1 = s1.replaceAll("[^a-zA-Z!]", "");
                String normalized2 = s2.replaceAll("[^a-zA-Z!]", "");
                return normalized1.compareTo(normalized2);
            }
        });
        removeDuplicateLetters(arr);
        List<String> filteredArr = new ArrayList<>();
        for (String element : arr) {
            if (!element.isEmpty()) {
                filteredArr.add(element);
            }
        }
        for (int i = 0; i < filteredArr.size(); i++) {
            String element = filteredArr.get(i);
            if (element.startsWith("!") && element.length() > 2) {
                String a = element.substring(1, 2);
                String b = element.substring(2);
                if (filteredArr.contains(a) && filteredArr.contains(b)) {
                    filteredArr.remove(element);
                }
            }
        }
        Set<String> set = new LinkedHashSet<>(Arrays.asList(arr));
        return set.toArray(new String[set.size()]);
    }

    public static void removeDuplicateLetters(String[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        String[] result = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            StringBuilder sb = new StringBuilder();
            Set<String> set = new HashSet<>();
            boolean negate = false;
            for (int j = 0; j < arr[i].length(); j++) {
                String c = String.valueOf(arr[i].charAt(j));
                if (c.equals("!")) {
                    negate = true;
                } else {
                    if (negate) {
                        c = "!" + c;
                        negate = false;
                    }
                    if (!set.contains(c)) {
                        sb.append(c);
                        set.add(c);
                    }
                }
            }
            result[i] = sb.toString();
        }
    }

    public static String[] NewOrder(String[] bFunction, String[] order) {
        List<String> result = new ArrayList<>();
        for (String letter : order) {
            boolean found = false;
            for (String element : bFunction) {
                if (element.contains(letter)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                result.add(letter);
                break;
            }
        }
        return result.toArray(new String[0]);
    }

    public static char[] convertToCharArray(String[] arr) {
        char[] charArr = new char[arr.length];
        for (int i = 0; i < arr.length; i++) {
            charArr[i] = arr[i].charAt(0);
        }
        return charArr;
    }

    public static String[] filterStringsLow(String[] bFunc, String[] order) {
        char[] c = convertToCharArray(order);
        List<String> result = new ArrayList<>();
        for (String string : bFunc) {
            boolean containsChar = false;
            boolean containsExclamationChar = false;
            for (char ch : c) {
                if (string.contains(String.valueOf(ch))) {
                    containsChar = true;
                    if (string.contains("!" + ch)) {
                        containsExclamationChar = true;
                        break;
                    }
                }
            }
            if (containsChar && !containsExclamationChar) {
                continue;
            }
            String newString = string.replace("!" + String.valueOf(c), "");
            result.add(newString);
        }
        if (result.isEmpty() || result.get(0).equals("")) {
            result.clear();
            result.add("0");
        }
        return result.toArray(new String[0]);
    }

    public static String[] filterStringsHigh(String[] bFunc, String[] order) {
        List<String> result = new ArrayList<>();

        for (String element : bFunc) {
            boolean containsSearchLetter = false;
            boolean isNegated = false;

            for (String searchLetter : order) {
                if (element.contains(searchLetter)) {
                    int index = element.indexOf(searchLetter);
                    if (index > 0 && element.charAt(index - 1) == '!') {
                        isNegated = true;
                    }
                    containsSearchLetter = true;
                }
            }

            if (containsSearchLetter) {
                if (!isNegated) {
                    result.add(element.replaceFirst(order[0], ""));
                }
            } else {
                result.add(element);
            }
        }
        if (result.isEmpty() || result.get(0).equals("")) {
            result.clear();
            result.add("1");
        }

        return result.toArray(new String[0]);
    }

    public BDD BDD_create(String bfunkcia, String order) {
        String[] bFunkcia = splitBfunc(bfunkcia);
        bFunkcia = sortStrings(bFunkcia);
        String[] Order = order.split("");
        low = new BDDNode();
        low.F = new String[]{"0"};
        high = new BDDNode();
        high.F = new String[]{"1"};
        root = new BDDNode();
        root.F = bFunkcia;
        root = BDDCreate(root, bFunkcia, Order);
        return this;
    }

    public BDDNode BDDCreate(BDDNode node, String[] bfunkcia, String[] order) {

        if (node.lowChild == null) {
            boolean OneLetter = checkOneLetter(bfunkcia);
            if (OneLetter) {
                boolean hasNeg = false;
                boolean hasNoNeg = false;
                boolean One = false;
                boolean Zero = false;
                for (String str : bfunkcia) {
                    if (str.contains("1")) {
                        One = true;
                    } else if (str.contains("0")) {
                        Zero = true;
                    } else if (str.contains("!")) {
                        hasNeg = true;
                    } else {
                        hasNoNeg = true;
                    }
                }
                if (One) {
                    node.parent.highChild = high;
                    node.parent = BDDCreate(node.parent, node.parent.F, order);
                } else if (Zero) {
                    node.parent.lowChild = low;
                    node.parent = BDDCreate(node.parent, node.parent.F, order);
                } else if (hasNeg && hasNoNeg) {
                    node.parent.highChild = high;
                    node.parent = BDDCreate(node.parent, node.parent.F, order);
                } else if (hasNeg) {
                    node.lowChild = high;
                    node.highChild = low;
                    node.parent = BDDCreate(node.parent, node.parent.F, order);
                } else {
                    node.lowChild = low;
                    node.highChild = high;
                    node.parent = BDDCreate(node.parent, node.parent.F, order);
                }
            } else {
                String[] neworder =NewOrder(bfunkcia, order);
                bfunkcia = filterStringsLow(bfunkcia, neworder);
                bfunkcia = sortStrings(bfunkcia);
                BDDNode n = new BDDNode();
                n.F = bfunkcia;
                n.id = neworder;
                BDDNode check = nodeMap.get(n.getF()+n.getId());
                if(check==null)
                {
                    nodeMap.put(n.getF()+n.getId(),n);
                    n.parent = node;
                    node.lowChild = n;
                    BDDCreate(n, bfunkcia, order);
                }
                else {

                        check.parent = node;
                        node.lowChild = check;
                        BDDCreate(node, node.F, order);
                }
            }
        } else if (node.highChild == null) {
            boolean OneLetter = checkOneLetter(bfunkcia);
            if (OneLetter) {
                boolean hasNeg = false;
                boolean hasNoNeg = false;
                boolean One = false;
                boolean Zero = false;
                for (String str : bfunkcia) {
                    if (str.contains("1")) {
                        One = true;
                    } else if (str.contains("0")) {
                        Zero = true;
                    } else if (str.contains("!")) {
                        hasNeg = true;
                    } else {
                        hasNoNeg = true;
                    }
                }
                if (One) {
                    node.parent.highChild = high;
                    node.parent = BDDCreate(node.parent, node.parent.F, order);
                } else if (Zero) {
                    node.parent.lowChild = low;
                    node.parent = BDDCreate(node.parent, node.parent.F, order);
                } else if (hasNeg && hasNoNeg) {
                    node.parent.highChild = high;
                    node.parent = BDDCreate(node.parent, node.parent.F, order);
                } else if (hasNeg) {
                    node.lowChild = high;
                    node.highChild = low;
                    node.parent = BDDCreate(node.parent, node.parent.F, order);
                } else {
                    node.lowChild = low;
                    node.highChild = high;
                    node.parent = BDDCreate(node.parent, node.parent.F, order);
                }
            } else {
                String[] neworder =NewOrder(bfunkcia, order);
                bfunkcia = filterStringsHigh(bfunkcia, neworder);
                bfunkcia = sortStrings(bfunkcia);
                BDDNode n = new BDDNode();
                n.F = bfunkcia;
                n.id = neworder;
                BDDNode check = nodeMap.get(n.getF()+n.getId());
                if(check==null)
                {
                    nodeMap.put(n.getF()+n.getId(),n);
                    n.parent = node;
                    node.highChild = n;
                    BDDCreate(n, bfunkcia, order);
                }
                else {
                    check.parent = node.parent;
                    node.highChild = check;
                    BDDCreate(check, check.F, order);

                }
            }
        } else {
            if (node == root)
                return node;
            else if (node.parent == null) {
                root = node;
                return node;
            }
            node.parent = BDDCreate(node.parent, node.parent.F, order);
        }
        return node;
    }

    //-------------------------------------
    public BDD BDD_create_with_best_order(String bfunkcia) {
        Set<Character> variables = extractVariables(bfunkcia);
        List<Character> orderedVars = new ArrayList<>(variables);

        // Find optimal variable ordering using Quine-McCluskey algorithm
        List<Character> optimalOrder = findOptimalOrder(bfunkcia, orderedVars);

        // Create BDD with optimal variable ordering
        return BDD_create(bfunkcia, optimalOrder.toString());
    }

    public static Set<Character> extractVariables(String bfunkcia) {
        Set<Character> variables = new HashSet<>();
        for (char c : bfunkcia.toCharArray()) {
            if (Character.isLetter(c)) {
                variables.add(c);
            }
        }
        return variables;
    }

    public List<Character> findOptimalOrder(String bfunkcia, List<Character> variables) {
        List<Character> optimalOrder = new ArrayList<>();
        int minNodes = Integer.MAX_VALUE;

        // Generate all possible variable orderings
        List<List<Character>> orders = generateOrderings(variables);

        // Find ordering with minimum number of nodes
        for (List<Character> order : orders) {
            BDD bdd = BDD_create(bfunkcia, order.toString());
            int nodes = bdd.count(root);
            if (nodes < minNodes) {
                minNodes = nodes;
                optimalOrder = order;
            }
        }

        return optimalOrder;
    }

    public static List<List<Character>> generateOrderings(List<Character> variables) {
        if (variables.size() == 1) {
            return Collections.singletonList(variables);
        }

        List<List<Character>> orderings = new ArrayList<>();
        for (int i = 0; i < variables.size(); i++) {
            Character variable = variables.get(i);
            List<Character> remaining = new ArrayList<>(variables);
            remaining.remove(i);
            for (List<Character> subOrdering : generateOrderings(remaining)) {
                List<Character> ordering = new ArrayList<>();
                ordering.add(variable);
                ordering.addAll(subOrdering);
                orderings.add(ordering);
            }
        }

        return orderings;
    }

    //---------------------------------
    public static String BDD_use(BDD bdd, String inputs) {
        BDDNode node = bdd.root;
        for (int i = 0; i < inputs.length(); i++) {
            char inputChar = inputs.charAt(i);
            if (inputChar == '0') {
                node = node.lowChild;
            } else if (inputChar == '1') {
                node = node.highChild;
            } else {
                return "-1";
            }
            if (node == null) {
                return "-1";
            }
        }
        if (Objects.equals(node.id, new String[]{"1"})) {
            return "1";
        } else if (Objects.equals(node.id, new String[]{"0"})) {
            return "0";
        } else {
            return "-1";
        }
    }
    public int count(BDDNode node)
    {
        Set<BDDNode> n =new HashSet<>();
        return counter(node,n);
    }
    private  int counter(BDDNode node, Set<BDDNode> n)
    {
        if(node == null|| n.contains(node))
        {
            return 0;
        }
        n.add(node);
        int count =1;
        count+=counter(node.getLow(),n);
        count+= counter(node.getHigh(),n);
        return count;
    }

    public static String generateBooleanFunction(int numVariables) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        int numWords = numVariables + rand.nextInt(50);
        char prevLetter = ' ';
        // generate random boolean function
        for (int i = 0; i < numWords; i++) {
            char letter = (char) ('A' + i % numVariables);
            sb.append(letter);
            if (rand.nextBoolean()) {
                sb.append("!");
            }
            if (i > 0 && (prevLetter == 'A' || prevLetter == 'B' || prevLetter == 'C') && (letter == 'D' || letter == 'E' || letter == 'F')) {
                sb.insert(sb.length() - 1, "+");
            }
            prevLetter = letter;
        }

        // remove unnecessary negation
        if (sb.charAt(sb.length() - 1) == '!') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }



}
