package pl.poznan.put.controller.followers.buttons;

import javafx.scene.control.Button;
import pl.poznan.put.model.follower.Follower;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class FollowButton extends Button {
    private static final EntityManager entityManager = EntityManagerProvider.getEntityManager();
    private final Follower follower;
    private boolean folowerExist;

    public FollowButton(User folower, User folowee) {
        super();
        this.follower = new Follower(folower,folowee);
        folowerExist = followerExists();
        if(!folowerExist){
            setText("follow");
        }else{
            setText("unfollow");
        }
        this.setOnAction(a -> this.onAction());
    }
    private void onAction(){
        if(!folowerExist){
            followAction();
            folowerExist = true;
            setText("unfollow");
        }
        else{
            unFollowAction();
            folowerExist = false;
            setText("follow");
        }
    }

    private void followAction(){
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(follower);
        transaction.commit();
    }
    private void unFollowAction(){
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(entityManager.contains(follower) ? follower : entityManager.merge(follower));
        transaction.commit();
    }
    private boolean followerExists(){
        Query query = entityManager.createQuery("select follower from Follower follower where follower = :f", Follower.class);
        query.setParameter("f", this.follower);
        long count = query.getResultStream().count();
        return count > 0;
    }
}
