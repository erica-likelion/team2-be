package likelion.hackerthon.grocering.user.repository;

import likelion.hackerthon.grocering.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
