package org.pure4s.logger4s

import org.slf4j
import org.slf4j.LoggerFactory

trait LazyLogging {
  implicit lazy val logger: slf4j.Logger = LoggerFactory.getLogger(getClass.getName)
}
