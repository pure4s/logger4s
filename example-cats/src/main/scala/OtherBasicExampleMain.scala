import cats.effect.{IO, Sync}
import cats.implicits._
import org.pure4s.logger4s.cats.Logger

case class User(email: String)

class UserService[F[_]: Sync: Logger] {
  def findByEmail(email: String): F[Option[User]] =
    Logger[F].info(s"User email is $email") *> Option(User(email)).pure[F]
}

object OtherBasicExampleMain extends App {
  implicit val instance: Logger[IO] = Logger.instance[IO](classOf[UserService[IO]])

  val service = new UserService[IO]
  service.findByEmail("example@example.com").unsafeRunSync()
}
