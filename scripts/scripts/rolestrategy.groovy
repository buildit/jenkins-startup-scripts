import com.michelin.cio.hudson.plugins.rolestrategy.*
import hudson.security.*
import java.lang.reflect.Constructor
import java.lang.reflect.Method

if (config) {
    RoleBasedAuthorizationStrategy authorizationStrategy = new RoleBasedAuthorizationStrategy()
    instance.setAuthorizationStrategy(authorizationStrategy)
    setRoles(authorizationStrategy, config)
    instance.save()
}

private setRoles(authorizationStrategy, strategy) {
    Constructor[] ctors = Role.class.getConstructors()
    for (Constructor<?> c : ctors) {
        c.setAccessible(true);
    }

    Method assignRoleMethod = RoleBasedAuthorizationStrategy.class.getDeclaredMethod("assignRole", String.class, Role.class, String.class)
    assignRoleMethod.setAccessible(true)

    strategy?.roles?.each { role ->
        println "Creating role ${role.name} with permissions ${role.permissions} for members ${role.members}"

        def newPermissions = new HashSet<Permission>()
        for (String permissionId : role.permissions) {
            newPermissions.add(Permission.fromId(permissionId))
        }

        Role newRole = new Role(role.name, newPermissions)
        authorizationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, newRole)

        role?.members?.each { newMember ->
            authorizationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, newRole, newMember)
        }

        println "Created role ${role.name}"
    }
}
