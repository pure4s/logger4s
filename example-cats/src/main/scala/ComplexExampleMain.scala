import cats.Show
import cats.effect.{IO, Sync}
import cats.implicits._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import org.json4s.{Formats, NoTypeHints}
import org.pure4s.logger4s.LazyLogging
import org.pure4s.logger4s.cats.Logger
import org.pure4s.logger4s.cats.Logger._

case class Client(email: String)

object Client {
  implicit val formats: Formats = Serialization.formats(NoTypeHints)
  implicit val showAsJson = new Show[Client] {
    override def show(client: Client): String = write(client)
  }
}

class ClientService[F[_]: Sync] extends LazyLogging {
  import Client._

  def findByEmail(email: String): F[Option[Client]] = {
    val client = Client(email)
    Logger[F].info(client) *> Option(client).pure[F]
  }
}

object ComplexExampleMain extends App {
  val service = new ClientService[IO]
  service.findByEmail("example@example.com").unsafeRunSync()
}
