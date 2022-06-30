package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TissuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tissu.class);
        Tissu tissu1 = new Tissu();
        tissu1.setId(1L);
        Tissu tissu2 = new Tissu();
        tissu2.setId(tissu1.getId());
        assertThat(tissu1).isEqualTo(tissu2);
        tissu2.setId(2L);
        assertThat(tissu1).isNotEqualTo(tissu2);
        tissu1.setId(null);
        assertThat(tissu1).isNotEqualTo(tissu2);
    }
}
