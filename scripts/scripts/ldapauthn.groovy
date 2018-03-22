import jenkins.model.*
import hudson.security.*
import hudson.util.*
import org.jenkinsci.plugins.*
import jenkins.security.plugins.ldap.*

String server = configOrDie('config?.server')
String rootDN = configOrDie('config?.rootDN')
String bindDn = configOrDie('config?.bindDN')
String bindPassword = configOrDie('config?.bindPass')
String userSearchBase = configOrDefault('config?.userSearchBase', 'cn=users,cn=accounts')
String userSearch = configOrDefault('config?.userSearch', 'uid={0}')
String groupSearchBase = configOrDefault('config?.groupSearchBase', 'cn=groups,cn=accounts')
String groupSearchFilter = configOrDefault('config?.groupSearchFilter', '')
String groupMembershipFilter = configOrDefault('config?.groupMembershipFilter', 'memberOf')
boolean inhibitInferRootDN = configOrDefault('config?.inhibitInferRootDN', false)
boolean disableMailAddressResolver = configOrDefault('config?.disableMailAddressResolver', false)
String displayNameAttributeName = configOrDefault('config?.displayNameAttributeName', 'displayname')
String mailAddressAttributeName = configOrDefault('config?.mailAddressAttributeName', 'mail')

def ldapRealm = new LDAPSecurityRealm(
        server,
        rootDN,
        userSearchBase,
        userSearch,
        groupSearchBase,
        groupSearchFilter,
        new FromUserRecordLDAPGroupMembershipStrategy(groupMembershipFilter), //LDAPGroupMembershipStrategy groupMembershipStrategy,
        bindDn,
        Secret.fromString(bindPassword),
        inhibitInferRootDN,
        disableMailAddressResolver,
        null,  //LDAPSecurityRealm.CacheConfiguration cache
        null,  //LDAPSecurityRealm.EnvironmentProperty[] environmentProperties,
        displayNameAttributeName,
        mailAddressAttributeName,
        IdStrategy.CASE_INSENSITIVE,
        IdStrategy.CASE_INSENSITIVE
)

Jenkins.instance.setSecurityRealm(ldapRealm)
Jenkins.instance.save()

def configOrDie(String configValueName) {
    if (evaluate(configValueName)) {
        println("Using value from config: ${configValueName}")
        return evaluate(configValueName)
    }
    println("Config value ${configValueName} not set. Unable to continue.")
    throw new IllegalStateException("Config value ${configValueName} not set. Unable to continue.")
}

def configOrDefault(String configValueName, defaultValue) {
    if (evaluate(configValueName)) {
        println("Using value from config: ${configValueName}")
        return evaluate(configValueName)
    }
    println("Config value ${configValueName} not set. Using default value.")
    return defaultValue
}
