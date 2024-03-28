package server.Auth;

import io.javalin.security.RouteRole;

enum Role implements RouteRole { ADMIN, USER }