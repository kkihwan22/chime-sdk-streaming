package syntax;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class HashMapTest {

    @Test
    void test_hashmap() {
        Map<Long, User> map = new HashMap<>();
        User user1 = new User(100L, "개똥이");
        User user2 = new User(200L, "소똥이");

        map.put(user1.getId(), user1);
        map.put(user2.getId(), user2);


        User user3 = new User(200L, "소똥이2222");

        User findUser = map.get(user3.getId());

        Assertions.assertEquals(200L, findUser.getId());
    }
}

@Getter
@ToString
@EqualsAndHashCode
class User {
    private Long id;
    private String nickname;

    public User(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
