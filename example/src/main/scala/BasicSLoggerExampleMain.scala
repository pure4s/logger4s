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
