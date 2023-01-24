package com.example.routing.plugin

import com.example.routing.dto.UserRole
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.application.install
import io.ktor.server.auth.AuthenticationChecked
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext

val RoleBasedAuthorization = createRouteScopedPlugin(
    name = "AuthorizationPlugin",
    createConfiguration = ::PluginConfiguration
) {
    val roles = pluginConfig.roles
    val getRole = pluginConfig.getRole
    pluginConfig.apply {
        on(AuthenticationChecked) { call ->
            val role = getRole(call.principal<JWTPrincipal>())
            if (role !in roles) {
                call.respondText("You are not allowed to visit this page", status = HttpStatusCode.Forbidden)
            }
        }
    }
}

fun Route.withRole(role: UserRole, build: Route.() -> Unit) =
    authorizedRoute(requiredRoles = setOf(role), build = build)

class AuthorizedRouteSelector(private val description: String) : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant

    override fun toString(): String = "(authorize ${description})"
}

private fun Route.authorizedRoute(
    requiredRoles: Set<UserRole>,
    build: Route.() -> Unit
): Route {

    val description = requiredRoles.joinToString(",")
    val authorizedRoute = createChild(AuthorizedRouteSelector(description))

    authorizedRoute.install(RoleBasedAuthorization) {
        roles = requiredRoles
        getRole = {
            it?.getClaim("role", UserRole::class)
        }
    }
    authorizedRoute.build()
    return authorizedRoute
}
class PluginConfiguration {
    var roles: Set<UserRole> = emptySet()
    lateinit var getRole : (principal: JWTPrincipal?) -> UserRole?
}