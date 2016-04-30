package api.business.entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mindaugas on 30/04/2016.
 */
@Entity
@Table(name = "transactiontypes", schema = "payment", catalog = "clubby")
public class TransactionType {
    private int transactiontypeid;
    private String name;

    @Id
    @Column(name = "transactiontypeid")
    public int getTransactiontypeid() {
        return transactiontypeid;
    }

    public void setTransactiontypeid(int transactiontypeid) {
        this.transactiontypeid = transactiontypeid;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionType that = (TransactionType) o;

        if (transactiontypeid != that.transactiontypeid)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = transactiontypeid;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    private Collection<MoneyTransaction> transactions;

    @OneToMany(mappedBy = "transactionType")
    public Collection<MoneyTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<MoneyTransaction> transactions) {
        this.transactions = transactions;
    }
}
