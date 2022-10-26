import com.common.timeout.TimeoutCenterApplication;
import com.common.timeout.api.TimeoutCenterService;
import com.common.timeout.api.dto.AddTimeoutTaskDTO;
import com.common.timeout.api.dto.TimeoutTaskVO;
import com.common.timeout.api.dto.WebResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * MqTest
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/4/25 15:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimeoutCenterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyTest {

    @Autowired
    private TimeoutCenterService timeoutCenterService;

    @Test
    public void test() {
        WebResponse<TimeoutTaskVO> webResponse1 = timeoutCenterService.queryTimeoutTask("abc", "def");
        WebResponse<TimeoutTaskVO> webResponse2 = timeoutCenterService.addTimeoutTask(new AddTimeoutTaskDTO());
        WebResponse<TimeoutTaskVO> webResponse3 = timeoutCenterService.cancelTimeoutTask("abc", "def");
    }
}
