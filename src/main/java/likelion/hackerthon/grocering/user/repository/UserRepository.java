package likelion.hackerthon.grocering.user.repository;

import likelion.hackerthon.grocering.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGuestId(String guestId);
}
