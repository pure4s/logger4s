package org.pure4s.logger4s.cats

import cats.Show
import cats.effect.Sync
import org.slf4j
import org.slf4j.LoggerFactory
import cats.implicits._
import org.pure4s.logger4s.{Logger => CoreLogger}

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

  def instance[F[_] : Sync](clazz: Class[_])
    (implicit logger: slf4j.Logger = LoggerFactory.getLogger(clazz)): Logger[F] = new Logger[F] {

    def error(msg: String): F[Unit]                     = Sync[F].delay(logger.error(msg))
    def error(msg: String, err: Throwable): F[Unit]     = Sync[F].delay(logger.error(msg, err))
    def warn(msg: String): F[Unit]                      = Sync[F].delay(logger.warn(msg))
    def info(msg: String): F[Unit]                      = Sync[F].delay(logger.info(msg))
    def debug(msg: String): F[Unit]                     = Sync[F].delay(logger.debug(msg))

    def error[A: Show](msg: A): F[Unit]                 = error(msg.show)
    def error[A: Show](msg: A, err: Throwable): F[Unit] = error(msg.show, err)
    def warn[A: Show](msg: A): F[Unit]                  = warn(msg.show)
    def info[A: Show](msg: A): F[Unit]                  = info(msg.show)
    def debug[A: Show](msg: A): F[Unit]                 = debug(msg.show)

  }

  implicit def instance[F[_] : Sync](implicit logger: slf4j.Logger): Logger[F] = new Logger[F] {

    def error(msg: String): F[Unit]                     = Sync[F].delay(logger.error(msg))
    def error(msg: String, err: Throwable): F[Unit]     = Sync[F].delay(logger.error(msg, err))
    def warn(msg: String): F[Unit]                      = Sync[F].delay(logger.warn(msg))
    def info(msg: String): F[Unit]                      = Sync[F].delay(logger.info(msg))
    def debug(msg: String): F[Unit]                     = Sync[F].delay(logger.debug(msg))

    def error[A: Show](msg: A): F[Unit]                 = error(msg.show)
    def error[A: Show](msg: A, err: Throwable): F[Unit] = error(msg.show, err)
    def warn[A: Show](msg: A): F[Unit]                  = warn(msg.show)
    def info[A: Show](msg: A): F[Unit]                  = info(msg.show)
    def debug[A: Show](msg: A): F[Unit]                 = debug(msg.show)
  }
}
