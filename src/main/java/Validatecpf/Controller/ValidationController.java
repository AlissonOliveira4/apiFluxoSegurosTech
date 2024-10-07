package Validatecpf.Controller;

import User.viacep.Endereco;
import Validatecpf.Domain.User;
import Validatecpf.Service.CreateandValidateCPFUseCase;
import WireMock.WireMock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController //Indica que essa classe é um controller REST. Junção das anotações Controller e ResponseBody
//StateLess -> a cada nova requisição eu recebo todas as informações que eu preciso para fazer aquela funcionalidade que preciso (manda um token, depois da primeira vez para não estragar a experiência dele), StateFull  -> vai manter o estado de cada cliente
@RequiredArgsConstructor
@RequestMapping("/desafio")
public class ValidationController {

    @Autowired
    private CreateandValidateCPFUseCase useCase;


//    @PostMapping("")
//    public ResponseEntity<String> ValidateCPFPost(@RequestBody User body) {
//        return useCase.Validar(body);
//    }

    @PostMapping("/validar")
    public String ValidateCPFPost(@RequestBody User body){
        return useCase.Validar(body);
    }


    @GetMapping("/endereco")
    public ResponseEntity<?> retornarEndereco(@RequestBody User body){
        if (useCase.ValidarExistencia(body).equals("User encontrado!")){
            WireMock wiremock = useCase.wireMockRetorno(body.getCpf());
            if (wiremock != null) {
                Endereco endereco = useCase.enderecoRetorno(wiremock.getCEP());
                if (endereco != null) {
                    return ResponseEntity.status(200).body(endereco);
                }
                return ResponseEntity.status(404).body("Endereço deu errado!");
            }
            return ResponseEntity.status(404).body("WireMock não encontrado!");
        }
        return ResponseEntity.status(404).body("User não encontrado!");
    }

    @GetMapping("/buscar-endereco/{cep}")
    public ResponseEntity<?> retornarEndereco2(@PathVariable String cep) {
        return useCase.retornarEndereco2(cep);
    }
}