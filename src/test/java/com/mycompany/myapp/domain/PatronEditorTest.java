package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatronEditorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatronEditor.class);
        PatronEditor patronEditor1 = new PatronEditor();
        patronEditor1.setId(1L);
        PatronEditor patronEditor2 = new PatronEditor();
        patronEditor2.setId(patronEditor1.getId());
        assertThat(patronEditor1).isEqualTo(patronEditor2);
        patronEditor2.setId(2L);
        assertThat(patronEditor1).isNotEqualTo(patronEditor2);
        patronEditor1.setId(null);
        assertThat(patronEditor1).isNotEqualTo(patronEditor2);
    }
}
