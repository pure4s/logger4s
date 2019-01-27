package org.pure4s.logger4s

import cats.Show
import cats.effect.Sync
import org.slf4j
import org.slf4j.LoggerFactory
import cats.implicits._

trait Logger[F[_]] {
  def error[A: Show](msg: A): F[Unit]
  def error[A: Show](msg: A, err: Throwable): F[Unit]
  def warn[A: Show](msg: A): F[Unit]
  def info[A: Show](msg: A): F[Unit]
  def debug[A: Show](msg: A): F[Unit]
}

object Logger {

  implicit val showString = new Show[String] {
    override def show(t: String): String = t.show
  }

  def apply[F[_]](implicit F: Logger[F]): Logger[F] = F

  def instance[F[_] : Sync](clazz: Class[_]): Logger[F] = new Logger[F] {
    val log: slf4j.Logger = LoggerFactory.getLogger(clazz)

    override def error[A: Show](msg: A): F[Unit] = Sync[F].delay(log.error(msg.show))
    override def error[A: Show](msg: A, err: Throwable): F[Unit] = Sync[F].delay(log.error(msg.show))
    override def warn[A: Show](msg: A): F[Unit] = Sync[F].delay(log.warn(msg.show))
    override def info[A: Show](msg: A): F[Unit] = Sync[F].delay(log.info(msg.show))
    override def debug[A: Show](msg: A): F[Unit] = Sync[F].delay(log.debug(msg.show))
  }
}