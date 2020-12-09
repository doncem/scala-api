# Blaze server with fs2

Generic App code to provide fs2 with cats effects setup ready to use as a base for any API project

Just extend `MainApp` trait with your App object and it's good to go.

Override `allRoutes` and `errorHandler` to fit your needs.

Set of config yet to be orchestrated as it needs to fit per implementation
