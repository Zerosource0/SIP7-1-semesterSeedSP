package facades;

import entity.User;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import security.PasswordHash;

public class UserFacade {
  
  //private final  Map<String, User> users = new HashMap<>();
  private PasswordHash alg = new PasswordHash();
  private EntityManagerFactory emf = Persistence.createEntityManagerFactory("semesterSeedSPPU",null);
  public EntityManager getEM(){
      return emf.createEntityManager();
  }
  
  public UserFacade(){
      
      /*
    //Test Users
    User user = new User("user",alg.createHash("test"));
    user.AddRole("User");
    addUser(user);
    //users.put(user.getUserName(),user );
    User admin = new User("admin",alg.createHash("test"));
    admin.AddRole("Admin");
    addUser(admin);
    //users.put(admin.getUserName(),admin);
    
    User both = new User("user_admin",alg.createHash("test"));
    both.AddRole("User");
    both.AddRole("Admin");
    addUser(both);
    //users.put(both.getUserName(),both );
      */
  }
  
  public User addUser(User user){
      EntityManager em = getEM();
      em.getTransaction().begin();
      em.persist(user);
      em.getTransaction().commit();
      em.close();
      return user;
  }
  
  
  public User getUserByUserId(String id){
    //return users.get(id);
    EntityManager em = getEM();
    return em.find(User.class, Integer.parseInt(id));
  }
  
    public User getUserByUserName(String userName){
    //return users.get(id);
    EntityManager em = getEM();
    return (User)em.createQuery("SELECT c from User c where c.userName = :userName").setParameter("userName", userName).getSingleResult();
  }
  
  /*
  Return the Roles if users could be authenticated, otherwise null
  */
  public List<String> authenticateUser(String userName, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
    User user = getUserByUserName(userName);
    return user != null && alg.validatePassword(password,user.getPassword()) ? user.getRoles(): null;
  }
  
}
