/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;


import Entity.User;
import Entity.Formation;
import utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import interfaces.iService_formation;
import java.sql.Timestamp;

/**
 *
 * @author User
 */



public class FormationService implements iService_formation<Formation>{

    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    private ResultSet rs;

    public FormationService() {
        
   
        try {
            cnx = DataSource.getInstance().getCnx();
            ste = cnx.createStatement();
           
        } catch (SQLException ex) {
            Logger.getLogger(FormationService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    
    
    
    /* hedhy ba3ed login 7aseb user connecter
    public List<formation> getVideos(int owner) {
        String req = "select * from video inner join user on(video.owner=user.id) where owner=" + owner;
        List<formation> list = new ArrayList<>();
        try {
            ste = cnx.createStatement();
            rs = ste.executeQuery(req);

            while (rs.next()) {
                list.add(new Formation(rs.getInt("id"), rs.getString("url"), rs.getString("title"), rs.getTimestamp("publish_date"),
                        new User(rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("sexe"),
                                rs.getString("adresse"),
                                rs.getString("name"),
                                rs.getString("first_name"),
                                rs.getString("telephone_number"),
                                rs.getString("bio"),
                                rs.getString("roles"),
                                rs.getDate("birthday"),
                                rs.getString("profile_pic"))));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
*/
    
    
    
    
    
    
    
    
    @Override
    public boolean ajouterVideo(Formation v) {
        String url = "";
        String find = "select url from video where url = '" + v.getUrl() + "'";

        try {
            ste = cnx.createStatement();
            rs = ste.executeQuery(find);
            while (rs.next()) {
                url = rs.getString("url");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormationService.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (v.getUrl().equals(url)) {
            System.out.println("Formation exitst !");
            return false;
        } else {
            String sql = "insert into video(url,title,publish_date,description,domaine) values('" + v.getUrl() + "','" + v.getTitle() + "','" + v.getPublish_date() + "','" + v.getDescription() + "','" + v.getDomaine() + "')";
            try {
                ste = cnx.createStatement();
                ste.executeUpdate(sql);

                return true;
            } catch (SQLException ex) {
                Logger.getLogger(FormationService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return false;
    }
    
    
    
    
    
    
    
    @Override
     public void supprimerVideo(Formation v){
        String req="delete from video where id=?";
        try {
            pst=cnx.prepareStatement(req);
            pst.setInt(1,v.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
        }
    }

     
     
     
     
    @Override
    public void modifierVideo(int id, String url, String title, Timestamp publish_date, String description, String domaine, User owner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getVideoById() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Formation> afficherVideo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     
     
     
     

    
     
     
     
  
}

