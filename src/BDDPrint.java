import java.io.PrintStream;
public class BDDPrint {

    private BDDNode tree;

    public BDDPrint(BDDNode tree) {
        this.tree = tree;
    }

    private String traversePreOrder(BDDNode root) {

        if (root == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(root.getF());

        String pointerRight = "└──";
        String pointerLeft = (root.getHigh() != null) ? "├──" : "└──";

        traverseNodes(sb, "", pointerLeft, root.getLow(), root.getHigh() != null);
        traverseNodes(sb, "", pointerRight, root.getHigh(), false);

        return sb.toString();
    }

    private void traverseNodes(StringBuilder sb, String padding, String pointer, BDDNode node,
                               boolean hasRightSibling) {

        if (node != null) {

            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.getFun());

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("│  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = "└──";
            String pointerLeft = (node.getHigh() != null) ? "├──" : "└──";

            traverseNodes(sb, paddingForBoth, pointerLeft, node.getLow(), node.getHigh() != null);
            traverseNodes(sb, paddingForBoth, pointerRight, node.getHigh(), false);

        }

    }

    public void print(PrintStream os) {
        os.print(traversePreOrder(tree));
    }
}
