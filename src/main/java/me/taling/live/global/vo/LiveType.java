package me.taling.live.global.vo;

import lombok.Getter;
import me.taling.live.core.usecase.asset.ComponentFactory;
import me.taling.live.core.usecase.live.CreateComponent;
import me.taling.live.supports.SpringBeanContextSupport;

public enum LiveType implements ComponentFactory<CreateComponent> {

    INSTANCE("instanceCreateComponentImpl"),
    LESSON("lessonCreateComponentImpl"),
    MEETING("meetingCreateComponentImpl"),

    ;

    @Getter
    private String beanName;

    LiveType(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public CreateComponent getBean() {
        return (CreateComponent) SpringBeanContextSupport.getApplicationContext().getBean(this.getBeanName());
    }
}
