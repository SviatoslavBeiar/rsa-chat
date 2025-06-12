// ChatRepository.java
package org.example.rsachat.repo;

import org.example.rsachat.model.User;
import org.example.rsachat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ChatRepository extends JpaRepository<Chat,Long> {
    List<Chat> findByUserAOrUserB(User userA, User userB);
}