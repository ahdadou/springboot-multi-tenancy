package ma.multitenant.config.multitenency;

import java.util.Optional;

public class TenantContext {

    private static final ThreadLocal<TenantId> CURRENT_TENANT = new ThreadLocal<>();

    public static Optional<TenantId> getCurrentTenant() {
        return Optional.ofNullable(CURRENT_TENANT.get());
    }

    public static void setCurrentTenant(TenantId tenant) {
        CURRENT_TENANT.set(tenant);
    }
}
