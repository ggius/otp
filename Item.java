/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giuseppevitolo;

import java.io.Serializable;

/**
 *
 * @author giuseppe
 */
public class Item implements Serializable {
    private String titolo;
    private String nomeutente;
    private String password;

    public Item(String titolo, String nomeutente, String password) {
        this.titolo = titolo;
        this.nomeutente = nomeutente;
        this.password = password;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getNomeutente() {
        return nomeutente;
    }

    public void setNomeutente(String nomeutente) {
        this.nomeutente = nomeutente;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public int hashCode() {
        int hash = 5;
        hash = hash * 31 +(this.getNomeutente() == null ? 0:this.getNomeutente().hashCode());
        hash = hash + (this.getTitolo() == null ? 0:this.getTitolo().hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(obj.getClass() != obj.getClass()){
            return false;
        }
        Item o = (Item) obj;
        return this.getTitolo().equals(o.getTitolo()) && this.getNomeutente().equals(o.getNomeutente());
    }

    @Override
    public String toString() {
        return getTitolo()+getNomeutente()+getPassword();
    }
}
