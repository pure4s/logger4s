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

object BasicLazyLoggingExampleMain extends App {
  val service = new AuthService[IO]
  service.login("example@example.com","123").unsafeRunSync()
}
