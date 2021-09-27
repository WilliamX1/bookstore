package com.bookstore.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.*;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "carts")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@DynamicUpdate
@DynamicInsert
public class Cart {
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cart")
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<CartItem> cartItems = new ArrayList<>();

    /* 该方法用于向cart中加cartitem项 */
    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
    }
    /* 该方法用于向cart中删除cartitem项 */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteCartItem(CartItem cartItem) {
        for (int i = 0; i < this.cartItems.size(); i++) {
            if (this.cartItems.get(i).getId().toString().equals(cartItem.getId().toString())) {
                this.cartItems.remove(i);
                return;
            }
        }
    }

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private User user = null;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    private Integer id;

    public List<CartItem> getCartItems() {
        return this.cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
