package org.pure4s.logger4s.scalaz

import org.pure4s.logger4s.{Logger => CoreLogger}
import org.slf4j
import org.slf4j.LoggerFactory
import scalaz.zio.IO
import scalaz.Show
import scalaz.syntax.show._

trait GenericLogger[F[_]] {

  def error[A: Show](msg: A): F[Unit]
  def error[A: Show](msg: A, err: Throwable): F[Unit]
  def warn[A: Show](msg: A): F[Unit]
  def info[A: Show](msg: A): F[Unit]
  def debug[A: Show](msg: A): F[Unit]
}

trait Logger[F[_]] extends CoreLogger[F] with GenericLogger[F]

object Logger {

  def apply[F[_]](implicit F: Logger[F]): Logger[F] = F

  implicit def instance(clazz: Class[_])
    (implicit logger: slf4j.Logger = LoggerFactory.getLogger(clazz)) = new Logger[IO[Nothing, ?]] {
     def error(msg: String): IO[Nothing, Unit]                  = IO.sync(logger.error(msg))
     def error(msg: String, err: Throwable): IO[Nothing, Unit]  = IO.sync(logger.error(msg, err))
     def warn(msg: String): IO[Nothing, Unit]                   = IO.sync(logger.warn(msg))
     def info(msg: String): IO[Nothing, Unit]                   = IO.sync(logger.info(msg))
     def debug(msg: String): IO[Nothing, Unit]                  = IO.sync(logger.debug(msg))

    def error[A: Show](msg: A): IO[Nothing, Unit]                 = error(msg.shows)
    def error[A: Show](msg: A, err: Throwable): IO[Nothing, Unit] = error(msg.shows, err)
    def warn[A: Show](msg: A): IO[Nothing, Unit]                  = warn(msg.shows)
    def info[A: Show](msg: A): IO[Nothing, Unit]                  = info(msg.shows)
    def debug[A: Show](msg: A): IO[Nothing, Unit]                 = debug(msg.shows)
  }

  implicit def instance(implicit logger: slf4j.Logger) = new Logger[IO[Nothing, ?]] {
     def error(msg: String): IO[Nothing, Unit]                  = IO.sync(logger.error(msg))
     def error(msg: String, err: Throwable): IO[Nothing, Unit]  = IO.sync(logger.error(msg, err))
     def warn(msg: String): IO[Nothing, Unit]                   = IO.sync(logger.warn(msg))
     def info(msg: String): IO[Nothing, Unit]                   = IO.sync(logger.info(msg))
     def debug(msg: String): IO[Nothing, Unit]                  = IO.sync(logger.debug(msg))

    def error[A: Show](msg: A): IO[Nothing, Unit]                 = error(msg.shows)
    def error[A: Show](msg: A, err: Throwable): IO[Nothing, Unit] = error(msg.shows, err)
    def warn[A: Show](msg: A): IO[Nothing, Unit]                  = warn(msg.shows)
    def info[A: Show](msg: A): IO[Nothing, Unit]                  = info(msg.shows)
    def debug[A: Show](msg: A): IO[Nothing, Unit]                 = debug(msg.shows)
  }
}
