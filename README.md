# Logger4s

Logger4s is a wrapping [SLF4J](https://www.slf4j.org/) purely functional library for Scala. 
It's easy to use and does not force a specific target context. 
You can run your computations in any type `F[_]`.

## Prerequisites ##

* Java 6 or higher
* Scala 2.11 or 2.12
* [Logback](http://logback.qos.ch) backend compatible with [SLF4J](https://www.slf4j.org/)

## Getting Logger4s ##

Logger4s is published to Sonatype OSS and Maven Central:

- Group id / organization: *org.pure4s*
- Artifact id / name: *logger4s*
- Latest version is 0.3.0

Usage with SBT, adding a dependency to the latest version of Logger4s to your `build.sbt`:

```scala
// For Scala 2.11, or 2.12
libraryDependencies += "org.pure4s" %% "logger4s-core"   % "0.3.0"  // Only if you want to support any backend
libraryDependencies += "org.pure4s" %% "logger4s-cats"   % "0.3.0"  // Cats ecosystem (cats-effect)
libraryDependencies += "org.pure4s" %% "logger4s-scalaz" % "0.3.0"  // Scalaz ecosystem (scalaz-zio)
```

## Using Logger4s ##

Example with LazyLogging and Cats:
```scala
import cats.effect.IO
import org.pure4s.logger4s.LazyLogging
import org.pure4s.logger4s.cats.Logger
import org.pure4s.logger4s.cats.Logger._

object Main extends App with LazyLogging {
  Logger[IO].info(s"Hello word, functional logger").unsafeRunSync()
  //2019-03-03 21:34:04.880 [BasicExampleMain$][INFO ] Hello word, purely functional logger
}
```

Example without LazyLogging and Cats:
```scala
import cats.effect.{IO, Sync}
import cats.implicits._
import org.pure4s.logger4s.cats.Logger

case class User(email: String)

class UserService[F[_] : Sync : Logger] {
  def findByEmail(email: String): F[Option[User]] = {
    Logger[F].info(s"User email is $email") *> Option(User(email)).pure[F]
  }
}

object Main extends App {
  implicit val instance: Logger[IO] = Logger.instance[IO](classOf[UserService[IO]])

  val service = new UserService[IO]
  service.findByEmail("example@example.com").unsafeRunSync()
  //2019-03-03 21:35:35.286 [UserService][INFO ] User email is example@example.com 
}
```

Example for comprehensions with LazyLogging and Cats
```scala
import cats.effect.{IO, Sync}
import cats.implicits._
import org.pure4s.logger4s.{LazyLogging, Logger}
import org.pure4s.logger4s.Logger._

case class Session(email: String, token: String)

class AuthService[F[_] : Sync] extends LazyLogging {

  def login(email: String, password: String): F[Session] = {

    def recoveryStrategy: PartialFunction[Throwable, F[Session]] = {
      case error =>
        Logger[F].error(s"Error creating session", error) *> Sync[F].raiseError(error)
    }

    val computation = for {
      _       <- Logger[F].info(s"Login with email = $email and password = $password")
      session <- Session(email, "token").pure[F]
      _       <- Logger[F].info(s"Success login with session = $session")
    } yield session

    computation recoverWith recoveryStrategy
  }
}

object Main extends App {
  val service = new AuthService[IO]
  service.login("example@example.com","123").unsafeRunSync()
  //2019-01-27 21:40:40.557 [AuthService][INFO] - Login with email = example@example.com and password = 123
  //2019-01-27 21:40:40.557 [AuthService][INFO] - Success login with session = Session(example@example.com,token)
}
```

Advance example, custom `Show` with LazyLogging and Cats:
```scala
import cats.Show
import cats.effect.{IO, Sync}
import cats.implicits._
import org.json4s.{Formats, NoTypeHints}
import org.json4s.native.Serialization
import org.pure4s.logger4s.{LazyLogging, Logger}
import org.pure4s.logger4s.Logger._
import org.json4s.native.Serialization.write

case class Client(email: String)

object Client {
  implicit val formats: Formats = Serialization.formats(NoTypeHints)
  implicit val showAsJson = new Show[Client] {
    override def show(client: Client): String = write(client)
  }
}

class ClientService[F[_] : Sync] extends LazyLogging{
  import Client._

  def findByEmail(email: String): F[Option[Client]] = {
    val client = Client(email)
    Logger[F].info(client) *> Option(client).pure[F]
  }
}

object Main extends App {
  val service = new ClientService[IO]
  service.findByEmail("example@example.com").unsafeRunSync()
  //2019-01-27 21:25:26.150 [ClientService][INFO] - {"email":"example@example.com"}
}
```

Example with LazyLogging and Scalaz:
```scala
import org.pure4s.logger4s.LazyLogging
import scalaz.zio.{IO, RTS}
import org.pure4s.logger4s.scalaz.Logger

object Main extends App with RTS with LazyLogging {

  unsafeRun(Logger[IO[Nothing, ?]].info(s"Hello word, purely functional logger"))
  //2019-03-03 21:49:59.905 [BasicExampleMain$][INFO ] Hello word, purely functional logger
}
```

## Code of conduct

People are expected to follow the [conduct-code] when discussing the project on the available communication channels.

[conduct-code]: https://www.scala-lang.org/conduct/
