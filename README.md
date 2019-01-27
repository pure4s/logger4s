# logger4s

logger4s is purely functional Logger library for Scala. It's easy to use and does not force a specific target context. You can run your computations in any type `F[_]` that has an instance of cats-effect's `Sync[F]`.

## Installation

Add the following to your `build.sbt`.

```scala
// For Scala 2.11, or 2.12
libraryDependencies += "org.pure4s" %% "logger4s" % "0.1.1"
```
## Usage

Logger:
```scala
import cats.effect.{IO, Sync}
import cats.implicits._
import org.pure4s.logger4s.Logger

case class User(email: String)

class UserService[F[_] : Sync : Logger] {
  def findByEmail(email: String): F[Option[User]] = {
    Logger[F].info(s"Hello word functional logger ($email)") *> Option(User(email)).pure[F]
  }
}

object BasicExampleMain extends App {
  implicit val sync: Logger[IO] = Logger.sync[IO](classOf[UserService[IO]])

  val service = new UserService[IO]
  service.findByEmail("example@example.com").unsafeRunSync()
}
```

SLogger:
```scala
import cats.Show
import cats.effect.{IO, Sync}
import cats.implicits._
import org.json4s.{Formats, NoTypeHints}
import org.json4s.native.Serialization
import org.pure4s.logger4s.SLogger
import org.json4s.native.Serialization.write

case class Client(email: String)

object Client {
  implicit val formats: Formats = Serialization.formats(NoTypeHints)
  implicit val showAsJson = new Show[Client] {
    override def show(client: Client): String = write(client)
  }
}

class ClientService[F[_] : Sync : SLogger] {
  import Client._

  def findByEmail(email: String): F[Option[Client]] = {
    val client = Client(email)
    SLogger[F].info(client) *> Option(client).pure[F]
  }
}

object BasicSLoggerExampleMain extends App {
  implicit val instance: SLogger[IO] = SLogger.instance[IO](classOf[ClientService[IO]])

  val service = new ClientService[IO]
  service.findByEmail("example@example.com").unsafeRunSync()
}

```

## Code of conduct

People are expected to follow the [conduct-code] when discussing the project on the available communication channels.

[conduct-code]: https://www.scala-lang.org/conduct/
