package dev.loteria.gui.controllers;

import dev.loteria.models.Sorteio;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Exibe uma animação simples mostrando as bolas do sorteio sendo reveladas
 * progressivamente, um número a cada 2 segundos.
 */
public class SorteioAnimationController {

  @FXML
  private Label lblModalidade;

  @FXML
  private Label lblStatus;

  @FXML
  private FlowPane ballContainer;

  @FXML
  private Button btnFechar;

  private Sorteio sorteio;
  private final List<StackPane> ballNodes = new ArrayList<>();
  private final List<RotateTransition> spinningAnimations = new ArrayList<>();
  private Timeline revealTimeline;

  @FXML
  public void initialize() {
    btnFechar.setOnAction(e -> closeWindow());
  }

  public void setSorteio(Sorteio sorteio) {
    this.sorteio = sorteio;
    if (sorteio == null || sorteio.getModalidade() == null) {
      lblModalidade.setText("Sorteio");
      lblStatus.setText("Dados do sorteio indisponíveis.");
      btnFechar.setDisable(false);
      return;
    }

    lblModalidade.setText(sorteio.getModalidade().getNome());
    btnFechar.setDisable(true);
    prepareBalls();
    startReveal();
  }

  private void prepareBalls() {
    ballContainer.getChildren().clear();
    ballNodes.clear();
    spinningAnimations.forEach(Animation::stop);
    spinningAnimations.clear();

    int quantidade = sorteio.getModalidade().getNumerosSorteio();
    for (int i = 0; i < quantidade; i++) {
      StackPane ball = createBallNode("?");
      ballNodes.add(ball);
      ballContainer.getChildren().add(ball);
      RotateTransition rotate = new RotateTransition(Duration.millis(450), ball);
      rotate.setFromAngle(0);
      rotate.setToAngle(360);
      rotate.setCycleCount(Animation.INDEFINITE);
      rotate.setInterpolator(Interpolator.LINEAR);
      rotate.play();
      spinningAnimations.add(rotate);
    }
  }

  private void startReveal() {
    List<Integer> numerosOrdenados = new ArrayList<>(sorteio.getNumerosSorteados());

    if (numerosOrdenados.isEmpty()) {
      finalizeAnimation();
      return;
    }

    lblStatus.setText("Girando bolas...");
    revealTimeline = new Timeline();

    double intervalo = Math.min(1.0, 5.0 / numerosOrdenados.size());
    double acumulado = intervalo;

    for (int i = 0; i < numerosOrdenados.size(); i++) {
      final int index = i;
      final int numero = numerosOrdenados.get(i);
      Duration delay = Duration.seconds(acumulado);
      revealTimeline.getKeyFrames().add(new KeyFrame(delay, e -> revealBall(index, numero)));
      acumulado += intervalo;
    }

    revealTimeline.setOnFinished(e -> finalizeAnimation());
    revealTimeline.play();
  }

  private void revealBall(int index, int numero) {
    if (index >= ballNodes.size())
      return;

    StackPane ball = ballNodes.get(index);
    Label numberLabel = (Label) ball.getChildren().get(0);
    numberLabel.setText(String.format("%02d", numero));

    if (index < spinningAnimations.size()) {
      spinningAnimations.get(index).stop();
    }
    ball.setRotate(0);
    numberLabel.setRotate(0);

    ScaleTransition pop = new ScaleTransition(Duration.millis(300), ball);
    pop.setFromX(0.4);
    pop.setFromY(0.4);
    pop.setToX(1);
    pop.setToY(1);
    pop.play();

    lblStatus.setText("Bola " + (index + 1) + " sorteada!");
  }

  private void finalizeAnimation() {
    lblStatus.setText("Sorteio concluído!");
    btnFechar.setDisable(false);
  }

  private StackPane createBallNode(String text) {
    StackPane stack = new StackPane();
    stack.getStyleClass().add("ball");
    Label label = new Label(text);
    label.getStyleClass().add("ball-label");
    stack.getChildren().add(label);
    return stack;
  }

  private void closeWindow() {
    if (revealTimeline != null) {
      revealTimeline.stop();
    }
    spinningAnimations.forEach(Animation::stop);
    Stage stage = (Stage) btnFechar.getScene().getWindow();
    stage.close();
  }
}
