package br.com.fiap.analytics.adapter.input.scheduled;

import br.com.fiap.analytics.application.service.GerarRelatorioSemanalService;
import br.com.fiap.analytics.domain.RelatorioSemanal;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RelatorioSemanalJob {

    private static final Logger LOG = Logger.getLogger(RelatorioSemanalJob.class);

    @Inject
    GerarRelatorioSemanalService service;

    @Scheduled(cron = "0 0 8 ? * MON")
    public void executar() {
        RelatorioSemanal relatorio = service.executar();
        LOG.infof("Relatorio semanal: total=%d criticos=%d mediaNotas=%.2f",
                relatorio.total(), relatorio.criticos(), relatorio.mediaNotas());
    }
}
