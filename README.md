# logger4s

logger4s is purely functional Logger library for Scala. It's easy to use and does not force a specific target context. You can run your computations in any type `F[_]` that has an instance of cats-effect's `Sync[F]`.

## Installation

Add the following to your `build.sbt`.

```scala
// For Scala 2.11, or 2.12
libraryDependencies += "org.pure4s" %% "logger4s" % "0.1.0"
```
## Usage

Example:
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

## Code of conduct

People are expected to follow the [conduct-code] when discussing the project on the available communication channels.

[conduct-code]: https://www.scala-lang.org/conduct/
