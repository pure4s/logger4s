package org.pure4s.logger4s

import cats.effect.Sync
import org.slf4j
import org.slf4j.LoggerFactory

trait Logger[F[_]] {
  def error(message: String): F[Unit]
  def error(message: String, exception: Throwable): F[Unit]
  def warn(message: String):  F[Unit]
  def info(message: String):  F[Unit]
  def debug(message: String): F[Unit]
}



object Logger {

  def apply[F[_]](implicit F: Logger[F]): Logger[F] = F

  def sync[F[_] : Sync](clazz: Class[_]): Logger[F] = new Logger[F] {
    val log: slf4j.Logger = LoggerFactory.getLogger(clazz)

    def error(message: String): F[Unit]                   = Sync[F].delay(log.error(message))
    def error(message: String, error: Throwable): F[Unit] = Sync[F].delay(log.error(message, error))
    def warn(message: String):  F[Unit]                   = Sync[F].delay(log.warn(message))
    def info(message: String):  F[Unit]                   = Sync[F].delay(log.info(message))
    def debug(message: String): F[Unit]                   = Sync[F].delay(log.debug(message))
  }

}