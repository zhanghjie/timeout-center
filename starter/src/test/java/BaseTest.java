import com.common.timeout.TimeoutCenterApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * BaseTest
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/10/26 16:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimeoutCenterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {
}
