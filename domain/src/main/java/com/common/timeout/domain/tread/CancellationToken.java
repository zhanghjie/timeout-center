package com.common.timeout.domain.tread;

import lombok.Setter;

/**
 * CancellationToken
 * 功能描述：CancellationToken
 *
 * @author zhanghaojie
 * @date 2023/5/7 18:09
 */
public class CancellationToken {
    private volatile boolean cancelled;

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
