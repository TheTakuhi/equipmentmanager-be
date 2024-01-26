package com.interstellar.equipmentmanager.config.mapper;

import com.interstellar.equipmentmanager.model.dto.loan.in.LoanCreateDTO;
import com.interstellar.equipmentmanager.model.dto.loan.out.LoanCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.loan.out.LoanDTO;
import com.interstellar.equipmentmanager.model.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class LoanMapperConfig {
    private final ModelMapper mapper;

    private final Converter<Loan, UUID> loanToId = ctx -> ctx.getSource() == null ? null : ctx.getSource().getId();

    private final Converter<LoanCroppedDTO, UUID> loanDTOToId = ctx -> ctx.getSource() == null ? null : ctx.getSource().getId();

    private final Converter<UUID, Loan> idToLoan = ctx -> {
        if (ctx.getSource() == null) {
            return null;
        }
        var loan = new Loan();
        loan.setId(ctx.getSource());
        return loan;
    };

    private final Converter<LoanCreateDTO, LocalDate> convertDate = ctx -> {
        if (ctx.getSource() == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale( Locale.UK );
        LocalDate date = LocalDate.parse(ctx.getSource().getLoanDate(), formatter);
        return date;
    };

    @Bean
    public void LoanConverting() {
        mapper.typeMap(Loan.class, UUID.class).setConverter(loanToId);
        mapper.typeMap(LoanCroppedDTO.class, UUID.class).setConverter(loanDTOToId);
        mapper.typeMap(UUID.class, Loan.class).setConverter(idToLoan);
        mapper.typeMap(LoanCreateDTO.class, Loan.class).addMappings(
                em -> {
                    em.map(LoanCreateDTO::getBorrowerId, Loan::setBorrower);
                    em.map(LoanCreateDTO::getItemId, Loan::setItem);
                    em.using(convertDate).map(d -> d, Loan::setLoanDate);
                }
        );
        mapper.typeMap(Loan.class, LoanDTO.class).addMappings(
                em -> {
                    em.map(Loan::getBorrower, LoanDTO::setBorrowerCroppedDTO);
                    em.map(Loan::getLender, LoanDTO::setLenderCroppedDTO);
                    em.map(Loan::getItem, LoanDTO::setItemCroppedDTO);
                }
        );
        mapper.typeMap(Loan.class, LoanCroppedDTO.class).addMappings(
                em -> {
                    em.map(Loan::getBorrower, LoanCroppedDTO::setBorrowerId);
                    em.map(Loan::getLender, LoanCroppedDTO::setLenderId);
                    em.map(Loan::getItem, LoanCroppedDTO::setItemId);
                }
        );
    }
}