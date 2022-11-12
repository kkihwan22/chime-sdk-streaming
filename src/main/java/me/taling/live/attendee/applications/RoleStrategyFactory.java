package me.taling.live.attendee.applications;

import me.taling.live.supports.SpringBeanContextSupport;

public class RoleStrategyFactory {
    private enum EnumFactory {
        MANAGER {
            @Override
            protected RoleStrategy createRoleStrategy() {
                return (RoleStrategy) SpringBeanContextSupport.getBeanByName("managerRoleStrategy");
            }
        },
        TUTEE {
            @Override
            protected RoleStrategy createRoleStrategy() {
                return (RoleStrategy) SpringBeanContextSupport.getBeanByName("tuteeRoleStrategy");
            }
        }
        ;

        public RoleStrategy create() {
            return createRoleStrategy();
        }

        abstract protected RoleStrategy createRoleStrategy();
    }

    public static RoleStrategy factory(String strategy) {
        return EnumFactory.valueOf(strategy).create();
    }
}