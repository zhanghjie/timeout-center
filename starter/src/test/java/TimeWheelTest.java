import com.common.timeout.infrastructure.timewheel.TimerWheelService;
import com.common.timeout.infrastructure.timewheel.vo.TimerTask;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TimeWheelTest
 * 功能描述：TODO
 *
 * @author zhanghaojie
 * @date 2022/10/26 16:29
 */
public class TimeWheelTest extends BaseTest {

    @Autowired
    private TimerWheelService timerWheelService;

    @Test
    public void TestWheel() throws Exception {
        Long time = System.currentTimeMillis();
        System.out.println("当前时间" + time);
        for (int t = 0; t < 10; t++) {
            TimerTask timerTask = new TimerTask("biz", String.valueOf(t), time + 4000, 1);
            timerWheelService.add(timerTask);
        }
        Thread.sleep(40000L);
    }

    public static void main(String[] args) {
        TimerTask biz = new TimerTask("biz", "123", 4000L, 1);
        System.out.println(ClassLayout.parseInstance(biz).toPrintable());
    }
}
