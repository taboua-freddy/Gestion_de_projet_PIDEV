/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.activites;

import entity.user.User;

/**
 *
 * @author Freddy
 */
public  class UserFx extends User {

    private Boolean Selected;
    private User user;

    public UserFx(User u) {
        super(u.getIdUser(), u.getNom(), u.getPrenom(), u.getEmail());
        user = new User(u.getIdUser(), u.getNom(), u.getPrenom(), u.getEmail());
        this.Selected = false;
    }

    public UserFx(User u, boolean selected) {
        super(u.getIdUser(), u.getNom(), u.getPrenom(), u.getEmail());
        user = new User(u.getIdUser(), u.getNom(), u.getPrenom(), u.getEmail());
        this.Selected = selected;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean Selected) {
        this.Selected = Selected;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

