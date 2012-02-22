/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.dsl
import scala.collection.mutable.WrappedArray
import org.springframework.integration.dsl.builders._
import java.util.UUID

/**
 * @author Oleg Zhurakousky
 */
object DSL {

  implicit def anyComponent[T <: BaseIntegrationComposition] = new ComposableIntegrationComponent[T] {
    def compose(i: IntegrationComposition, s: T): T = {
      val mergedComposition =
        if (s.parentComposition != null) {
          val copyComposition = s.copy()
          i.merge(copyComposition)
          i.generateComposition(copyComposition.parentComposition, copyComposition.target)
        } 
        else i.generateComposition(i, s.target)

      mergedComposition.asInstanceOf[T]
    }
  }

  /**
   *
   */
  implicit def channelComponent = new ComposableIntegrationComponent[ChannelIntegrationComposition] {
    def compose(i: IntegrationComposition, c: ChannelIntegrationComposition) = {
      new ChannelIntegrationComposition(i, c.target)
    }
  }

  /**
   *
   */
  implicit def pollableChannelComponent = new ComposableIntegrationComponent[PollableChannelIntegrationComposition] {
    def compose(i: IntegrationComposition, c: PollableChannelIntegrationComposition) = new PollableChannelIntegrationComposition(i, c.target)
  }

}
