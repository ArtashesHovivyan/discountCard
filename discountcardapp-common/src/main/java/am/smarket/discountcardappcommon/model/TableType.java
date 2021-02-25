package am.smarket.discountcardappcommon.model;

public enum TableType {

    USER (1, "USER"),
    CASH (2, "CASH"),
    CREDIT (3, "CREDIT"),
    MASSAGE (4, "MASSAGE");

    private final Integer key;
    private final String value;

    TableType(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}
