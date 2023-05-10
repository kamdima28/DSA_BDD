public class BDDNode {
    String[] F;
    BDDNode lowChild;
    BDDNode highChild;
    BDDNode parent;
    String[] id;

    public BDDNode(String[] F, BDDNode low, BDDNode high) {
        this.F = F;
        this.lowChild = low;
        this.highChild = high;
    }

    public BDDNode() {

    }
    public String getF() {
        if (F == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < F.length; i++) {
            sb.append(F[i]);
            if (i < F.length - 1) {
                sb.append(" + ");
            }
        }
        return "(" + getId() + ")" + sb.toString();
    }
    public String getFun() {
        if (F == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < F.length; i++) {
            sb.append(F[i]);
            if (i < F.length - 1) {
                sb.append(" + ");
            }
        }
        return "(" + getId() + ")" + sb.toString() + " (" + this + ")";
    }
    public String getId(){
        if (id == null) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < id.length; i++) {
            sb.append(id[i]);
            if (i < id.length - 1) {
                sb.append(" + ");
            }
        }
        return sb.toString();
    }
    public String[] getVariableIndex() {
        return id;
    }

    public BDDNode getLow() {
        return lowChild;
    }
    public BDDNode getParent() {
        return parent;
    }

    public void setLow(BDDNode lowChild) {
        this.lowChild = lowChild;
    }

    public BDDNode getHigh() {
        return highChild;
    }

    public void setHigh(BDDNode highChild) {
        this.highChild = highChild;
    }
}
