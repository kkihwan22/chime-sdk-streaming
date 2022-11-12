package me.taling.live.api.create;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import me.taling.live.api.asset.BaseRestController;
import me.taling.live.api.asset.wrapper.SuccessResponseWrapper;
import me.taling.live.api.create.request.CreateRequest;
import me.taling.live.api.create.response.CreateResponse;
import me.taling.live.core.usecase.live.CreateComponent;
import me.taling.live.core.usecase.live.CreateComponent.CreateResult;
import me.taling.live.global.vo.LiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lives")
public class CreateRestController implements BaseRestController {
    private final Logger log = LoggerFactory.getLogger(CreateRestController.class);

    @ApiOperation(value = "생성", notes = "rest api 요청에 의한 클래스룸 생성 기능입니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success")
    })
    @PostMapping
    public SuccessResponseWrapper<CreateResponse> create(
            @RequestBody @Valid CreateRequest request, BindingResult bindingResult) {
        log.debug("request : {}", request);
        if (bindingResult.hasErrors()) {
            handleError(bindingResult.getAllErrors());
        }

        // bean factory 부분.
        CreateComponent component = Optional.ofNullable(request.getLiveType())
                .orElse(LiveType.INSTANCE)
                .getBean();

        CreateResult result = component.execute(request.of());
        log.debug("result:{}", result);
        return SuccessResponseWrapper.success(new CreateResponse(result));
    }
}