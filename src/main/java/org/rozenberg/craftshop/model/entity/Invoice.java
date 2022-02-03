package org.rozenberg.craftshop.model.entity;

import java.math.BigDecimal;

public class Invoice {
    private long invoiceId;
    private BigDecimal money;
    private BigDecimal discount;

    public Invoice(){}

    public Invoice(long invoiceId, BigDecimal money, BigDecimal discount) {
        this.invoiceId = invoiceId;
        this.money = money;
        this.discount = discount;
    }

    public Invoice(BigDecimal money, BigDecimal discount) {
        this.money = money;
        this.discount = discount;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Invoice newInvoice = (Invoice) obj;
        if (invoiceId != newInvoice.invoiceId) return false;
        if (money.compareTo(newInvoice.money) != 0) return false;
        return discount.compareTo(newInvoice.discount) == 0;
    }

    @Override
    public int hashCode() {
        int result = (int) (invoiceId ^ (invoiceId >>> 32));
        result = 31 * result + money.intValue();
        result = 31 * result + discount.intValue();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Invoice{");
        sb.append("invoiceId=").append(invoiceId);
        sb.append(", money=").append(money);
        sb.append(", discount=").append(discount);
        sb.append('}');
        return sb.toString();
    }
}
