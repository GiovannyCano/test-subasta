package cl.tuxpan.pruebaingreso.jobs;

import cl.tuxpan.pruebaingreso.repositories.ApuestaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CleanupJob {

  private final ApuestaRepository apuestaRepository;

  @Scheduled(cron = "0 */2 * * * *")
  public void run() {
    int rows = apuestaRepository.purgeApuestasCorruptas();
    if (rows > 0) {
      log.info("Cleanup: {} apuestas corruptas eliminadas", rows);
    } else {
      log.debug("Cleanup: 0 apuestas eliminadas");
    }
  }
}
