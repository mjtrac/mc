Now that NextGen Healthcare has decided to cease their support 
for the open versions of Mirth Connect, and now that there are 
two or more projects to continue to develop Mirth Connect, 
let's explore an alternative approach: using the capabilities
of the open source Apache Camel together with the capabilities
of the open source Spring Boot in order to move and modify
data following various protocols, including HL7v2, XML, JSON, 
FHIR and perhaps others.

Because so much is involved it can be overwhelming to try to
master everything at once.  So, for a first pass, I'm going
to attempt to show what actually needs to be done just to get
a basic setup up and running, without going into detail on 
what is going on.  My hope is that it will become evident 
that with very little effort, it is possible to create a
minimal solution for some basic use cases.

With that, we can begin.  You'll want to have a computer under
your control to do your learning with.  IT can throw up its 
roadblocks once you've played around.

First Steps

Our first approach is going to be modeled after some 
examples available at https://github.com/apache/camel-spring-boot-examples

We're going to use command line operations throughout, 
not the graphical user interface. You'll get used to this quickly, honest.

You will need to download Java 21 and a program called maven. 
You'll also want to install "git." For our purposes, git allows us
to download code collections from, among other places, a popular
software site named github.

On Windows,
the easiest approach might be to first install Chocolatey, a software manager.
Follow instructions for individual installation 
at https://chocolatey.org/install . 

Open a Powershell window with administrator privileges.
This will enable you to install Java 21 with the command line:
choco install openjdk --version=21
Following that, install maven with the command line:
choco install maven
And git:
choco install git

Note that the name of the maven command is actually mvn.


On a Mac, open a terminal window. 
Use homebrew.  First install it with the command:
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
Then, install Java 2 and maven:
brew install openjdk@21
brew link openjdk@21
brew install maven
brew install git

The second command line sets Java 21 as your default java.  

Note that homebrew's command is brew.

On Linux (Ubuntu or Debian), open a terminal window.
sudo apt update
sudo apt install openjdk-21-jdk
sudo apt install maven
sudo apt install git

On other Linux systems, use the package manager of the system, 
if apt is not available.

On any system:
You can confirm installation by asking each program for its version:
java -version
mvn -version
git --version

Note that git wants double-hyphens prior to "version"

Your next step will confirm you can download, build and run 
the camel spring boot examples from github.

On any system:
In your command window, enter the command:
git clone https://github.com/apache/camel-spring-boot-examples.git

This will create a directory (a folder) named camel-spring-boot-examples
underneath whatever folder the terminal or powershell had already been using, 
and will download a copy of the camel spring boot examples

In your command window, enter the command:
cd camel-spring-boot-examples 
which tells your terminal that you'd like to be using
your new camel-spring-boot-examples folder, unless you say otherwise.
It becomes your "current directory."

Next, enter another git command:
git checkout main

Next, in your command window, enter the command:
mvn install

What happens next should be a great deal of output indicating that you are 
building and testing the various examples. It will take long enough that 
now is a good time to take a break and stretch your legs.

When it's done, if all has gone well, you'll see a message BUILD SUCCESS.

If all has not gone well, sorry. Maven is driven by a file 
in the camel-spring-boot-examples directory named pom.xml, 
and about all I can suggest is that you figure out which example 
failed and remove it from the list of "modules" in that pom file 
and try again.

If you got your BUILD SUCCESS message, we can change to the directory for
one of the examples and try running it.

In your command window, enter the commands:
cd pojo
mvn package
and after another BUILD SUCCESS message:
mvn spring-boot:run

This will produce log output showing Spring and then Camel starting up and 
then starting a "route" called "route1":
 Started route1 (timer://number)

We don't need to worry about what the route does, honestly, we just care
that it starts and it appears to do something based on a repeating timer.

When we issued our request to maven to package up the application, it
produced a "jar" file, which we can run from the command line without using
maven.  To see this, enter the commands:
cd target
java -jar camel-example-spring-boot-pojo-4.10.2.jar

For that second command, use whatever name you find ending jar 
in the target folder. This runs the jar file as a standard Java application, 
and the output will be similar or identical.

Note that each example's top folder includes a file readme.adoc, which you 
can open in any text editor to see how to use the example.

Whichever way you run it, the program will terminate after a minute, or you
can press a key combination to kill it.  On Mac and Linux, Ctrl-C.  On Windows, whatever it is.

Let's recap what we've done:

We've installed Java 21, Maven, and Git, as well as a software manager suited
to our system.

We've downloaded from github some example code provided by the kind people 
at Apache Camel.

We've built the pojo example and run it, and seen that it runs a "route," 
whatever that might be.

Which means that we've established that our system, with the new downloads, 
can build and run Spring Boot Apache Camel applications and their routes.

Yay for us! 

If you're new to Java, Spring Boot, Camel, and the command line,
you probably want to stop here and play around a bit. You especially want
to become comfortable working with commands rather than the GUI. This will
pay you back a thousand times over. For starters, try using the up and down
arrows and see whether that recalls older commands. You might also try using the [tab] key when you've partially entered the name of a file. And you'll want
to become comfortable making different directories your "current directory" 
with the cd command. You can get told what your current directory is by 
typing the command "pwd" short for print working directory.  On some systems
you'll be able to use pushd and popd to keep track of the directory 
you were in when you decided you want another directory to be current.
And you can get to what your system thinks of as your "home" directory 
by typing cd ~. 

Now there's a whole lot of software and a whole lot of complexity behind 
the scenes, but we're going to start by avoiding all of that and instead
use a very basic Java program to load routes that we'll write in a 
straightforward language called yaml. Here's an example:

- route:
    id: timer-log-route
    from:
      uri: "timer:myTimer?period=5000"
      steps:
        - log: "Timer fired."

Yaml uses indentation to indicate hierarchies. The convention is two spaces. 
The hyphens indicate lists; both of the lists here have only one item.

The first line, unindented, says we are defining a route.  We can have
multiple such route definitions in our file, just as an article can have
multiple paragraphs.

Everything until the next unindented line is part of our route definition.
It's name (id) will be timer-log-route. Routes begin by specifying where
a message originates from.  This is the uri (or universal resource identifier)
line indented beneath from. The source might be a database or a file or a tcp 
connection, but here it's a component called "timer," and the component name
is the first part of its uri.

You can find documentation of timer at
this address: https://camel.apache.org/components/4.10.x/timer-component.html
or by searching the web for camel-timer. After the colon, we see that this
timer will be named myTimer.  After the question mark we have options, 
or parameters, that specify details of myTimer's behavior. In this case,
we are setting it to generate a message every 5000 milliseconds, every five
seconds. Once the message is generated, we may want to do any number
of things to it, and send it to any number of destinations.  These 
would be listed under steps.  In this case, we have only one step, we want
to log the fact that a message was sent. Log is a bit special. We could have
instead said "to:" and then provided a uri beginning log, but we're able to 
just say "log:" instead. 

The quoted material after log: is what we want sent
to be logged out. In our case, it's just text, but we could send the body
of the timer's message by including ${body} in the quoted text. That's 
because the quoted string is interpreted as in a language called Simple. 
We can use Simple, or we could use alternate languages appropriate to 
particular types of messages. For example, if our message was XML, we 
could specify that we wanted the expression to be interpreted as an xpath expression. 

To get our yaml route loaded by our sample program, we have to put it in 
a location that Camel will pick routes up from. This can be done in a file
typically named application.properties or application.yaml, which is typically
placed in a folder under your application's top folder: src/main/resources.

We create a file named application.properties in src/main/resources, and 
include one line:
camel.springboot.routes-include-pattern=classpath:routes/*.yaml

Now any file whose name ends yaml and is located in src/main/resources/routes
will be loaded when our application starts up, and the routes it defines
will be created and started.

A route is a pathway that a message will take from when it is first
picked up or generated (produced) by a Camel component until it has been
processed and sent on to be consumed by other Camel components. Camel
is astoundingly flexible in the ways it allows you to create and
interact with routes, but the basic idea is always the same. You get
a message at a from: and send it on with one or more to:'s, altering
it as you wish in-between.  The alterations can be defined using yaml,
xml, or Java. When using yaml or xml, you can include code snippets
from simple languages.  We've already seen that quoted text is interpreted
by a language called "Simple," which allows substitutions. When you include ${body} in a "Simple" expression, it is substituted with the actual body of the
message.  

Messages exist within Camel exchanges, allowing Camel to reply when a message
is sent. In addition to their bodies, messages have "headers" which can be
used to store information useful in interpreting what to do with the message,
and Exchanges have "properties" which serve a similar purpose. Exchanges will have in and out messages.

Camel components used as message sources ("from:") might go out and read
files from a folder or records from a database, or open a socket to which 
another application can send data.

In Mirth Connect, a simple "channel" might get HL7 messages from a port, 
interact with them in some way, and store them to a file or database. Camel
can do the same using the MLLP component. MLLP is short for minimal low level
protocol, and is a way of surrounding an HL7 version 2 message with flags
that say "message begins here" and "message ends here." So we can write
a route that will receive an HL7v2 message using the MLLP protocol by using
the mllp component as our "from:". While Mirth converts the message to XML,
Camel is more flexible; it can convert the message to XML or to various 
Java objects defined by HAPI, a library which is incorporated 
into Mirth as well. Rather than use e4x in Javascript, you can then use
HAPI calls to extract information. For example, there is a HAPI object
called a terser, which can give you back a patient's last name by 
interpreting the text "/.PID-5-2". 

Where in Mirth's e4x you might say
var lastName = msg['PID']['PID.5']['PID.5.2'].toString(), 
in Camel/groovy you can say
def lastName = terser("/.PID-5-2"). 
Instead of storing it in Mirth's channel map, you would store it 
in a Camel header.

Or you could use HAPI calls to go
to specific parts of a message using HL7 groups, segments, and fields, 
despite "groups" not being a visible level in XML representations.

def lastName = message.get("PID").getField(5).get(0).getFamilyName().getValue()
exchange.in.setHeader("PatientLastName", lastName)

There is a little bit of extra syntax, but a route could look like this:
- route:
    id: hl7-extraction-route
    from:
      uri: "mllp:localhost:8888"
    steps:
      - groovy:
          script: |
            def lastName = message.get("PID").getField(5).get(0).getFamilyName().getValue()
            exchange.in.setHeader("PatientLastName", lastName)
      
      - log:
          message: "Extracted patient last name: ${header.PatientLastName}"

It's true you don't have the drag-and-drop capability of the Mirth Connect
GUI, but such a GUI could always be developed if a healthcare community
develops around Apache Camel.  There is already a GUI for defining routes, 
the very capable Kaoto.

It is easier and better documented to develop new components 
for Apache Camel than to develop new plugins for Mirth Connect.
To see the published components, go to 
https://camel.apache.org/components/4.10.x/index.html

We can also extend routes with SSL support without needing to purchase anything.
We'll tackle that next. We need to provide an object of type 
SSLContextParameters. One alternative is to do this via Java, but let's
say we want to add SSL without needing to restart. In that case, 
we can take advantage of the ability to include groovy scripts 
directly in our route's processing. But first, let's allow ourselves
a restart, and define the SSLContextParameters creation in a 
configuration script executed upon startup. We then refer to the object
from our routes by adding parameters to the uri: 
&useSsl=true&sslContextParameters=#mySslParams. 

In this case, we're relying on the presence of an SSLContext 
registered with the name mySslParams.


application.yaml:

camel:
  springboot:
    name: my-camel-app

ssl:
  keystore:
    path: classpath:keystore.jks
    password: changeit
    key-password: changeit
  truststore:
    path: classpath:truststore.jks
    password: changeit
	
route config:
- script:
    id: define-ssl-context
    language: groovy
    script: |
      import org.apache.camel.support.jsse.*
      import org.springframework.core.env.Environment

      Environment env = context.registry.lookupByName("environment")

      def keystorePath = env.getProperty("ssl.keystore.path")
      def keystorePassword = env.getProperty("ssl.keystore.password")
      def keyPassword = env.getProperty("ssl.keystore.key-password")

      def truststorePath = env.getProperty("ssl.truststore.path")
      def truststorePassword = env.getProperty("ssl.truststore.password")

      def keyStoreParams = new KeyStoreParameters()
      keyStoreParams.setResource(keystorePath)
      keyStoreParams.setPassword(keystorePassword)

      def keyManagers = new KeyManagersParameters()
      keyManagers.setKeyStore(keyStoreParams)
      keyManagers.setKeyPassword(keyPassword)

      def trustStoreParams = new KeyStoreParameters()
      trustStoreParams.setResource(truststorePath)
      trustStoreParams.setPassword(truststorePassword)

      def trustManagers = new TrustManagersParameters()
      trustManagers.setKeyStore(trustStoreParams)

      def sslContextParams = new SSLContextParameters()
      sslContextParams.setKeyManagers(keyManagers)
      sslContextParams.setTrustManagers(trustManagers)

      context.registry.bind("mySslParams", sslContextParams)

- route:
    id: mllp-secure-route
    from:
      uri: "mllp://0.0.0.0:2575?autoAck=true&sync=true&useSsl=true&sslContextParameters=#mySslParams"
    steps:
      - log:
          message: "Received HL7 message: ${body}"
      - to: "direct:process-hl7"


One file, two routes, two ssl configs:
- script:
    id: define-multiple-ssl-contexts
    language: groovy
    script: |
      import org.apache.camel.support.jsse.*
      import org.springframework.core.env.Environment

      Environment env = context.registry.lookupByName("environment")

      def defineSsl(namePrefix) {
          def kp = new KeyStoreParameters()
          kp.setResource(env.getProperty("ssl.${namePrefix}.keystore.path"))
          kp.setPassword(env.getProperty("ssl.${namePrefix}.keystore.password"))

          def km = new KeyManagersParameters()
          km.setKeyStore(kp)
          km.setKeyPassword(env.getProperty("ssl.${namePrefix}.keystore.key-password"))

          def tp = new KeyStoreParameters()
          tp.setResource(env.getProperty("ssl.${namePrefix}.truststore.path"))
          tp.setPassword(env.getProperty("ssl.${namePrefix}.truststore.password"))

          def tm = new TrustManagersParameters()
          tm.setKeyStore(tp)

          def scp = new SSLContextParameters()
          scp.setKeyManagers(km)
          scp.setTrustManagers(tm)

          context.registry.bind("sslParams_${namePrefix}", scp)
      }

      defineSsl("route1")
      defineSsl("route2")

- route:
    id: route1-secure
    from:
      uri: "mllp://0.0.0.0:2575?useSsl=true&sslContextParameters=#sslParams_route1"
    steps:
      - log:
          message: "Route 1 received: ${body}"

- route:
    id: route2-secure
    from:
      uri: "mllp://0.0.0.0:2576?useSsl=true&sslContextParameters=#sslParams_route2"
    steps:
      - log:
          message: "Route 2 received: ${body}"


There is a great deal of ssl/tls configurability, but we've just accepted
the defaults.  You can read up on SSLContextParameters to learn about
additional configuration.

One large problem in using Apache Camel instead of Mirth Connect is that
there is a much wider range of options.  For example, there are a variety
of ways of interacting with HL7v2.  You could receive it as a string of text,
or you can use HAPI libraries to turn it into version and type specific
objects which you can then validate and interact with via all the methods
available on the object's classes.  Or you can use the HAPI terser as we
did above.  Or, if you want to closely mimic e4x at the expense of some 
efficiency, you can use something called XmlSlurper.

Here's one route to get an XML tree:
- route:
    id: hl7-to-xml
    from:
      uri: direct:convert
    steps:
      - unmarshal:
          hl7: {}
      - marshal:
          jacksonXml: {}
      - to: log:hl7-as-xml
	  
That tree may not be what you want, though.

Here's a route that works with XmlSlurper:
- route:
    id: hl7-pipe-to-xmlslurper
    from:
      uri: direct:start
    steps:
      - script:
          language: groovy
          script: |
            import ca.uhn.hl7v2.parser.*
            import ca.uhn.hl7v2.HapiContext
            import groovy.util.XmlSlurper

            def raw = exchange.in.body as String

            // Parse HL7 and convert to XML
            def hapiContext = new DefaultHapiContext()
            def parser = hapiContext.getPipeParser()
            def xmlParser = hapiContext.getXMLParser()

            def msg = parser.parse(raw)
            def hl7xml = xmlParser.encode(msg)

            // Use XmlSlurper on the structured XML
            def parsed = new XmlSlurper(false, false).parseText(hl7xml)

            // Extract OBX-5-1 values
            def obxValues = parsed.'**'.findAll { it.name() == 'OBX' }.collect { obx ->
              obx.'OBX.5'.'CE.1'.text()
            }

            exchange.message.body = obxValues

Let's say you want something more complex, perhaps a conversion from
HL7v2 to FHIR.  There are Java libraries that will do that, and you can
easily call them from Apache Camel.

We're at the point now where we need to understand a bit more about
that file named pom.xml at the root of our application's tree.  That
file is called a Project Object Model file, and for our purposes, it
instructs the maven build process about dependencies that our project
will have.  So every time we choose to use a new Apache Camel Spring Boot
component, we'll generally need to add its corresponding dependency.
These dependencies must specify a group and an artifact, and often 
specify a version.  For example, in order to use MLLP, we have to 
have a dependency on the component that provides that capability:

	<dependency>
		<groupId>org.apache.camel.springboot</groupId>
		<artifactId>camel-mllp-starter</artifactId>
	</dependency>

To use HAPI, we have to include dependencies on the HAPI library
and on the structures corresponding to a particular HL7v2 version:

	<dependency>
		<groupId>org.apache.camel.springboot</groupId>
		<artifactId>camel-hl7-starter</artifactId>
	</dependency>
	<dependency>
		<groupId>ca.uhn.hapi</groupId>
		<artifactId>hapi-base</artifactId>
		<version>2.5.1</version>
	</dependency>
	<dependency>
		<groupId>ca.uhn.hapi</groupId>
		<artifactId>hapi-structures-v251</artifactId>
		<version>2.5.1</version>
	</dependency>

In the same way, when you want to use a new library, you need
to add a dependency on it, whatever it may be.  Maven takes care
of getting you all the things your new dependency depends upon
itself. Here is Google's repository for healthcare data harmonization:
https://github.com/GoogleCloudPlatform/healthcare-data-harmonization

If you cannot find it in the public Maven Repositories, you'd build it
yourself and depend upon it:

	<dependency>
		<groupId>com.google.cloud.healthcare</groupId>
		<artifactId>hl7v2-fhir-converter</artifactId>
		<version>0.2.0</version> <!-- Replace with actual -->
	</dependency>
	
Once you have the dependencies specified, you can call upon the required
functionality from a Java bean, or write a groovy script and embed it
into your route:

- route:
    id: hl7v2-to-fhir-bundle
    from:
      uri: direct:hl7
    steps:
      - script:
          language: groovy
          script: |
            import com.google.fhir.gateway.Hl7v2ToFhirConverter
            import org.hl7.fhir.r4.model.Bundle

            // Only initialize converter once (cache in exchange property or static field)
            if (!context.registry.lookupByName("hl7ToFhirConverter")) {
              def configPath = "src/main/resources/hl7v2-fhir-mapping.json"
              def converter = Hl7v2ToFhirConverter.create(configPath)
              context.registry.bind("hl7ToFhirConverter", converter)
            }

            def converter = context.registry.lookupByName("hl7ToFhirConverter") as Hl7v2ToFhirConverter
            def hl7 = exchange.in.body as String

            // Convert to FHIR Bundle
            def bundle = converter.convert(hl7)

            // Set as new body
            exchange.message.body = bundle
      - to: log:fhir-bundle


These days, a great deal of functionality is provided to systems using tools 
like Docker or its open-source equivalent, Podman.  Let's say we wanted
to make our tool available via Docker.  The pom.xml already includes the 
necessary plugin, so all we need to do is (on Mac) brew install colima

4/25

Now have removed the Users and swagger approach to removed_from_mc tree; 
this didn't take effect until I did a clean.  
I have a RouteManagementController
and a RouteLoaderService which can list routes at localhost:8080/routes2 
and start and stop routes from buttons on that screen.
ChatGPT offers this to unload routes: 
public boolean unloadRoute(String routeId) throws Exception {
    camelContext.getRouteController().stopRoute(routeId);
    return camelContext.removeRoute(routeId);
}

and offers this to "persist" routes by saving them in a location
from which they'll be loaded at startup:

@PostMapping("/routes")
public ResponseEntity<String> uploadRoute(@RequestParam("file") MultipartFile file) throws IOException {
    String routeId = extractRouteId(file); // maybe from file name or YAML contents
    Path path = Paths.get("routes", routeId + ".yaml");
    Files.createDirectories(path.getParent());
    Files.write(path, file.getBytes());

    loadRouteFromYaml(path); // parses and adds route
    return ResponseEntity.ok("Route loaded and saved.");
}

and deletion:
public void deleteRouteYaml(String routeId) throws IOException {
    Files.deleteIfExists(Paths.get("routes", routeId + ".yaml"));
}

It uses a different approach to loading YAML routes than I do, what I do
is based on retrieving something from a PluginHelper and using it. 

Gemini, PluginHelper:
public class RouteLoaderExample {
    public void loadRoutesFromFile(CamelContext camelContext, String filePath) throws Exception {
        Path file = Paths.get(filePath);
        byte[] routeDefinitionBytes = Files.readAllBytes(file);
        Resource resource = ResourceHelper.fromBytes(file.toString(), routeDefinitionBytes);
        PluginHelper.getRoutesLoader(camelContext).loadRoutes(resource);
    }
}


ChatGPT:getRoutesLoader("yaml").loadRoutesBuilder(path)
    private void loadRouteFromYaml(Path path) throws Exception {
        RoutesBuilderLoader loader = camelContext.getCamelContextExtension().getRoutesLoader().getRoutesLoader("yaml");
        RoutesBuilder builder = loader.loadRoutesBuilder(path.toUri());
        camelContext.addRoutes(builder);
    }
	
I've also discovered that with the current pom, jmx (Java Management Extensions)
can be used to get the routes and exchange counts without code.

Info on adding mllp autosaving is here:
https://chatgpt.com/share/680bd007-4998-800c-b824-2d92d54a7d55

