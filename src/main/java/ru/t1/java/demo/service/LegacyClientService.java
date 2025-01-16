package ru.t1.java.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.client.ClientDto;
import ru.t1.java.demo.mapper.ClientMapper;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LegacyClientService {
    private final ClientRepository repository;
    private final ClientMapper clientMapper;
    private final Map<Long, Client> cache;

    public LegacyClientService(ClientRepository repository, ClientMapper clientMapper) {
        this.repository = repository;
        this.clientMapper = clientMapper;
        this.cache = new HashMap<>();
    }


    public ClientDto getClient(Long id) {
        log.debug("Call method getClient with id {}", id);
        ClientDto clientDto = null;

        if (cache.containsKey(id)) {
            return clientMapper.toDto(cache.get(id));
        }

        try {
            Client entity = repository.findById(id).get();
            clientDto = clientMapper.toDto(entity);
            cache.put(id, entity);
        } catch (Exception e) {
            log.error("Error: ", e);
//            throw new ClientException();
        }

//        log.debug("Client info: {}", clientDto.toString());
        return clientDto;
    }

}
