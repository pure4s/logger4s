package org.pure4s.logger4s.scalaz

import org.pure4s.logger4s.SLogger
import org.slf4j
import org.slf4j.LoggerFactory
import scalaz.zio.IO

trait Logger[F[_]] extends SLogger[F]

object Logger {

  def apply[F[_]](implicit F: Logger[F]): Logger[F] = F

  implicit def instance(clazz: Class[_])
    (implicit logger: slf4j.Logger = LoggerFactory.getLogger(clazz)) = new Logger[IO[Nothing, ?]] {
    override def error(msg: String): IO[Nothing, Unit]                  = IO.sync(logger.error(msg))
    override def error(msg: String, err: Throwable): IO[Nothing, Unit]  = IO.sync(logger.error(msg, err))
    override def warn(msg: String): IO[Nothing, Unit]                   = IO.sync(logger.warn(msg))
    override def info(msg: String): IO[Nothing, Unit]                   = IO.sync(logger.info(msg))
    override def debug(msg: String): IO[Nothing, Unit]                  = IO.sync(logger.debug(msg))
  }

  implicit def instance(implicit logger: slf4j.Logger) = new Logger[IO[Nothing, ?]] {
    override def error(msg: String): IO[Nothing, Unit]                  = IO.sync(logger.error(msg))
    override def error(msg: String, err: Throwable): IO[Nothing, Unit]  = IO.sync(logger.error(msg, err))
    override def warn(msg: String): IO[Nothing, Unit]                   = IO.sync(logger.warn(msg))
    override def info(msg: String): IO[Nothing, Unit]                   = IO.sync(logger.info(msg))
    override def debug(msg: String): IO[Nothing, Unit]                  = IO.sync(logger.debug(msg))
  }
}
